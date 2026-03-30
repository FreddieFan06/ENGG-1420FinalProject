package service;

import java.time.LocalDateTime;
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

    private final UserService userService;
    private final EventService eventService;
    private final WaitlistService waitlistService;

    public BookingService(UserService us, EventService es, WaitlistService ws) {
        this.userService = us; this.eventService = es; this.waitlistService = ws;
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
}