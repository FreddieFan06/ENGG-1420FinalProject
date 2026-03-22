// Created by Shivom Bhanot (1319202)

package validation.rules;

import exception.ValidationException;

// Business rule that ensures a user has not exceeded their maximum number of active bookings
public class UserBookingLimitRule implements ValidationRule<BookingContext> {

    @Override
    public void validate(BookingContext context) {

        // If the user has reached or exceeded their max bookings, throw an exception
        if (context.getUser().getMaxBookings() <= context.getUserActiveBookings()) {
            throw new ValidationException("User has reached their active booking limit.");
        }
    }
}