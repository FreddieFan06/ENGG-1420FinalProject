package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class EventNullException extends BookingException {
    public EventNullException(String message) {
        super(message);
    }
    
}
