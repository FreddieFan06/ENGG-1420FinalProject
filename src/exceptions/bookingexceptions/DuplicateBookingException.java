package exceptions.bookingexceptions;

public class DuplicateBookingException extends BookingException {
    public DuplicateBookingException(String message) {
        super(message);
    }
    
}
