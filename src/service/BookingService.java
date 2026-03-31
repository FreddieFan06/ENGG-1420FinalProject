package service;

import io.DataWriter;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import model.bookings.Booking;
import model.enums.BookingStatus;

import model.events.Event;
import model.users.User;
import validation.rules.*;

public class BookingService {
    private final Map<String, Booking> bookingRegistry = new HashMap<>();
    private final Map<String, Integer> confirmedCounts = new HashMap<>();
    private final Map<String, String> duplicateCheck = new HashMap<>();

    private String bookingCsvPath;
    private boolean persistEnabled;

    private final UserService userService;
    private final EventService eventService;
    private final WaitlistService waitlistService;

    public BookingService(UserService us, EventService es, WaitlistService ws) {
        this.userService = us;
        this.eventService = es;
        this.waitlistService = ws;
        this.persistEnabled = false;
    }

    public void enablePersistence(String csvPath) {
        this.bookingCsvPath = csvPath;
        this.persistEnabled = true;
    }

    private void saveBookings() {
        if (!persistEnabled || bookingCsvPath == null || bookingCsvPath.isBlank()) {
            return;
        }
        try {
            DataWriter.saveBookings(bookingCsvPath, getAllBookings());
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist bookings: " + e.getMessage(), e);
        }
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

    public int getAttendeeCount(String eventId) {
        return confirmedCounts.getOrDefault(eventId, 0);
    }

    public Collection<Booking> getAllBookings() {
        return bookingRegistry.values();
    }

    public Booking registerForEvent(String userId, String eventId) {
        if (userId == null || userId.isBlank() || eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("User ID and Event ID are required.");
        }

        if (!userService.userExists(userId)) {
            throw new IllegalArgumentException("User does not exist: " + userId);
        }

        Event event = eventService.getEvent(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event does not exist: " + eventId);
        }

        String key = userId + "_" + eventId;
        if (duplicateCheck.containsKey(key)) {
            throw new exceptions.bookingexceptions.DuplicateBookingException("User is already booked or waitlisted for this event.");
        }

        int confirmed = getAttendeeCount(eventId);
        int userActiveBookings = (int) bookingRegistry.values().stream()
                .filter(b -> b.getUserId().equals(userId) && b.getBookingStatus() == BookingStatus.CONFIRMED)
                .count();

        BookingContext context = new BookingContext(userService.getUser(userId), event, userActiveBookings, confirmed);
        var rules = List.of(new EventStatusRule(), new UserBookingLimitRule());

        // If event is full the capacity rule should not trigger exception, we will place user into waitlist.
        new ValidatorEngine<>(rules).validate(context);

        BookingStatus status = BookingStatus.CONFIRMED;
        if (confirmed >= event.getCapacity()) {
            status = BookingStatus.WAITLISTED;
        }

        String bookingId = "B" + (bookingRegistry.size() + 1);
        Booking booking = new Booking(bookingId, userId, eventId, LocalDateTime.now(), status);
        booking.validate();

        bookingRegistry.put(bookingId, booking);
        duplicateCheck.put(key, bookingId);

        if (status == BookingStatus.CONFIRMED) {
            confirmedCounts.merge(eventId, 1, Integer::sum);
        } else {
            waitlistService.addToWaitlist(eventId, booking);
        }

        saveBookings();

        return booking;
    }
}
