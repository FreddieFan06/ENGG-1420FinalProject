package validation;

import exception.ValidationException;
import model.bookings.Booking;
import model.events.Event;
import model.users.User;

import java.time.LocalDateTime;

public final class BookingValidator {

    public static void validateBookingInput(String bookingId, String userId, String eventId, LocalDateTime createdAt) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new ValidationException("Booking ID is required.");
        }
        if (userId == null || userId.isBlank()) {
            throw new ValidationException("User ID is required.");
        }
        if (eventId == null || eventId.isBlank()) {
            throw new ValidationException("Event ID is required.");
        }
        if (createdAt == null) {
            throw new ValidationException("Booking creation date is required.");
        }
    }

    public void validateBookingObject(Booking booking) {
        if (booking == null) {
            throw new ValidationException("Booking object cannot be null.");
        }
        validateBookingInput(
                booking.getBookingId(),
                booking.getUserId(),
                booking.getEventId(),
                booking.getCreatedAt());
    }

    public void validateBookingRules(User user, Event event, int userActiveBookings, int confirmedCount) {
        if (user == null) {
            throw new ValidationException("User does not exist.");
        }
        if (event == null) {
            throw new ValidationException("Event does not exist.");
        }
        if (event.getStatus() != null && event.getStatus().name().equals("CANCELLED")) {
            throw new ValidationException("Cannot book a cancelled event.");
        }

        if (userActiveBookings >= user.getMaxBookings()) {
            throw new ValidationException("User has reached their active booking limit.");
        }

    }
}