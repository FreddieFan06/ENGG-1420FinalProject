package model.bookings;

import model.enums.BookingStatus;
import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String userId;
    private String eventId;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;

    public Booking(String bookingId, String userId, String eventId, LocalDateTime createdAt, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.bookingStatus = bookingStatus;
    }

    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getEventId() { return eventId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BookingStatus getBookingStatus() { return bookingStatus; }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}