package exceptions.userexceptions;

import exceptions.bookingexceptions.BookingException;

public class InvalidEmailException extends BookingException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
