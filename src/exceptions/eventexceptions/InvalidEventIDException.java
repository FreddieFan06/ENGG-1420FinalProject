package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class InvalidEventIDException extends BookingException {
    public InvalidEventIDException(String message) {
        super(message);
    }
}
