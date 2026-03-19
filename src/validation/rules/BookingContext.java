// Created by Shivom Bhanot (1319202)

package validation.rules;

import model.bookings.Booking;
import model.events.Event;
import model.users.User;

// Context object holding all data needed for booking-related business rule validation
public class BookingContext {

    private final User user; // The user trying to make the booking
    private final Event event; // The event being booked
    private final int userActiveBookings; // Number of active bookings the user currently has
    private final int confirmedCount; // Number of confirmed bookings for the event

    public BookingContext(User user, Event event, int userActiveBookings, int confirmedCount) {
        this.user = user;
        this.event = event;
        this.userActiveBookings = userActiveBookings;
        this.confirmedCount = confirmedCount;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public int getUserActiveBookings() {
        return userActiveBookings;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }
}