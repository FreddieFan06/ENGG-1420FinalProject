package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class InvalidEventTitleException extends BookingException {
    public InvalidEventTitleException(String message) {
        super(message);
    }
}
