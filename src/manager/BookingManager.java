package manager;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.bookingexceptions.BookingException;
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

public class BookingManager {
    private Map<String, Booking> bookingRegistry;

    private UserManager userManager;
    private EventManager eventManager;
    private WaitlistManager waitlistManager;
    private AccessControlService accessControlService;

    public BookingManager(UserManager userManager, EventManager eventManager, WaitlistManager waitlistManager) {
        this.bookingRegistry = new HashMap<>();
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.waitlistManager = waitlistManager;
    }

    private int countActiveBookingsForUser(String userId) {
        int count = 0;
        for (Booking b : bookingRegistry.values()) {
            if (b.getUserId().equals(userId) && b.getBookingStatus() == BookingStatus.CONFIRMED)
                count++;
        }
        return count;
    }

    public Booking getBooking(String bookingId) {
        return bookingRegistry.get(bookingId);
    }

    private int getConfirmedCountForEvent(String eventId) {
        int count = 0;
        for (Booking b : bookingRegistry.values()) {
            if (b.getEventId().equals(eventId) && b.getBookingStatus() == BookingStatus.CONFIRMED)
                count++;
        }
        return count;
    }

    public void addExistingBooking(Booking booking) {
        if (booking != null) {
            bookingRegistry.put(booking.getBookingId(), booking);
        }
    }

    public Collection<Booking> getAllBookings() {
        return bookingRegistry.values();
    }

    public Booking createBooking(String bookingId, String userId, String eventId, LocalDateTime createdAt) {
        User user = userManager.getUser(userId);
        Event event = eventManager.getEvent(eventId);

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
        } catch (BookingException e) {
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

        if (assignedStatus == BookingStatus.WAITLISTED) {
            waitlistManager.addToWaitlist(eventId, newBooking);
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
            waitlistManager.removeFromWaitlist(eventId, bookingToCancel.getUserId());
        else if (previousStatus == BookingStatus.CONFIRMED) {
            Booking promotedBooking = waitlistManager.popNextFromWaitlist(eventId);

            if (promotedBooking != null) {
                promotedBooking.setBookingStatus(BookingStatus.CONFIRMED);
                System.out.println("AUTOMATIC PROMOTION: User " + promotedBooking.getUserId() +
                        " is now CONFIRMED for Event " + eventId);
            }
        }

        return true;
    }
}