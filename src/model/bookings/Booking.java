package model.bookings;

import java.time.LocalDateTime;

import model.enums.BookingStatus;
import validation.Validatable;
import validation.ValidationUtils;

public class Booking implements Validatable {
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

    // Self-validation method
    @Override
    public void validate() {
        ValidationUtils.requireNonBlank(bookingId, "Booking ID is required.");
        ValidationUtils.requireNonBlank(userId, "User ID is required.");
        ValidationUtils.requireNonBlank(eventId, "Event ID is required.");
        ValidationUtils.requireNonNull(createdAt, "Booking creation date is required.");
        ValidationUtils.requireNonNull(bookingStatus, "Booking status is required.");
    }
}