package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class EventCancelledException extends BookingException {
    public EventCancelledException(String message) {
        super(message);
    }
}
