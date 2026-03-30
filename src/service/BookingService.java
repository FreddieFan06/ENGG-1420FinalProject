package service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.ValidationException;
import model.bookings.Booking;
import model.enums.BookingStatus;
import model.events.Event;
import model.users.User;
import security.AccessControlService;
import validation.rules.BookingContext;
import validation.rules.EventCapacityRule;
import validation.rules.EventStatusRule;
import validation.rules.UserBookingLimitRule;
import validation.rules.ValidatorEngine;

public class BookingService {
    private final Map<String, Booking> bookingRegistry = new HashMap<>();
    private final Map<String, Integer> confirmedCounts = new HashMap<>();
    private final Map<String, String> duplicateCheck = new HashMap<>();

    // Tracks waitlist history for analytics
    private Map<String, Boolean> wasWaitlistedMap;

    private UserService userService;
    private EventService eventService;
    private WaitlistService waitlistService;
    private AccessControlService accessControlService;

    public BookingService(UserService userService, EventService eventService, WaitlistService waitlistService) {
        this.bookingRegistry = new HashMap<>();

        // Initialize analytics tracking map
        this.wasWaitlistedMap = new HashMap<>();

        this.userService = userService;
        this.eventService = eventService;
        this.waitlistService = waitlistService;
    }

    public void addExistingBooking(Booking b) {
        bookingRegistry.put(b.getBookingId(), b);
        duplicateCheck.put(b.getUserId() + "_" + b.getEventId(), b.getBookingId());
        if (b.getBookingStatus() == BookingStatus.CONFIRMED) {
            confirmedCounts.merge(b.getEventId(), 1, Integer::sum);
        }
    }

    public List<Booking> getConfirmedAttendees(String eventId) {
        return bookingRegistry.values().stream()
            .filter(b -> b.getEventId().equals(eventId) && b.getBookingStatus() == BookingStatus.CONFIRMED)
            .collect(Collectors.toList());
    }

    public List<Booking> getWaitlistedAttendees(String eventId) {
        return bookingRegistry.values().stream()
            .filter(b -> b.getEventId().equals(eventId) && b.getBookingStatus() == BookingStatus.WAITLISTED)
            .collect(Collectors.toList());
    }

    public void addExistingBooking(Booking booking) {
        if (booking != null) {
            bookingRegistry.put(booking.getBookingId(), booking);
        }
    }

    public Collection<Booking> getAllBookings() {
        return bookingRegistry.values();
    }

    // Check if booking was ever waitlisted
    public boolean wasEverWaitlisted(String bookingId) {
        return wasWaitlistedMap.getOrDefault(bookingId, false);
    }

    public Booking createBooking(String bookingId, String userId, String eventId, LocalDateTime createdAt) {
        User user = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);

        // --- START: New OOP validation ---
        int currentActive = countActiveBookingsForUser(userId);
        int currentConfirmed = getConfirmedCountForEvent(eventId);

        BookingContext context = new BookingContext(user, event, currentActive, currentConfirmed);
        ValidatorEngine<BookingContext> engine = new ValidatorEngine<>(
                List.of(
                        new UserBookingLimitRule(),
                        new EventCapacityRule(),
                        new EventStatusRule()));

        try {
            engine.validate(context);
        } catch (ValidationException e) {
            System.err.println("Booking failed: " + e.getMessage());
            return null;
        }
        // --- END: New OOP validation ---

        // Keep your original logic for duplicate booking check
        for (Booking b : bookingRegistry.values()) {
            if (b.getUserId().equals(userId) && b.getEventId().equals(eventId)
                    && b.getBookingStatus() != BookingStatus.CANCELLED) {
                System.err.println("Booking failed: User is already booked or waitlisted for this event.");
                return null;
            }
        }

        // Assign status (confirmed vs waitlisted)
        BookingStatus assignedStatus = currentConfirmed < event.getCapacity()
                ? BookingStatus.CONFIRMED
                : BookingStatus.WAITLISTED;

        Booking newBooking = new Booking(bookingId, userId, eventId, createdAt, assignedStatus);
        bookingRegistry.put(bookingId, newBooking);

        // Mark booking as waitlisted (analytics)
        if (assignedStatus == BookingStatus.WAITLISTED) {
            wasWaitlistedMap.put(bookingId, true);
            waitlistService.addToWaitlist(eventId, newBooking);
        }

        return newBooking;
    }

    public boolean cancelBooking(String bookingId) {
        Booking bookingToCancel = bookingRegistry.get(bookingId);

        if (bookingToCancel == null || bookingToCancel.getBookingStatus() == BookingStatus.CANCELLED)
            return false;

        BookingStatus previousStatus = bookingToCancel.getBookingStatus();
        bookingToCancel.setBookingStatus(BookingStatus.CANCELLED);

        String eventId = bookingToCancel.getEventId();

        if (previousStatus == BookingStatus.WAITLISTED)
            waitlistService.removeFromWaitlist(eventId, bookingToCancel.getUserId());
        else if (previousStatus == BookingStatus.CONFIRMED) {
            Booking promotedBooking = waitlistService.popNextFromWaitlist(eventId);

            if (promotedBooking != null) {
                // Promote waitlisted booking to confirmed
                promotedBooking.setBookingStatus(BookingStatus.CONFIRMED);
                System.out.println("AUTOMATIC PROMOTION: User " + promotedBooking.getUserId() +
                        " is now CONFIRMED for Event " + eventId);
            }
        }

        return true;
    }
}