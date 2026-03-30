package exceptions.userexceptions;

import exceptions.bookingexceptions.BookingException;

public class UserBookingLimitException extends BookingException {
    public UserBookingLimitException(String message) {
        super(message);
    }
    
}