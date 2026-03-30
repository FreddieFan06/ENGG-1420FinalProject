package exceptions.eventexceptions;

import exceptions.bookingexceptions.BookingException;

public class InvalidEventStatusException extends BookingException {
    public InvalidEventStatusException(String message) {
        super(message);
    }
    
}
