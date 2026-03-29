package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class InvalidEventCapacityException extends BookingException {
    public InvalidEventCapacityException(String message) {
        super(message);
    }
    
}
