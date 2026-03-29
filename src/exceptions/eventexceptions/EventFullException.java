package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class EventFullException extends BookingException {
    public EventFullException(String message) {
        super(message);
    }
    
}
