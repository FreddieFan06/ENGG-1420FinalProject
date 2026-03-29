package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class DuplicateEventIDException extends BookingException {
    public DuplicateEventIDException(String message) {
        super(message);
    }
}
