package manager;

import model.bookings.Booking;
import model.events.Event;
import model.users.User;
import model.enums.BookingStatus;
import model.enums.EventStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.time.LocalDateTime;

public class BookingManager {
    private Map<String, Booking> bookingRegistry;

    private UserManager userManager;
    private EventManager eventManager;
    private WaitlistManager waitlistManager;

    public BookingManager(UserManager userManager, EventManager eventManager, WaitlistManager waitlistManager) {
        this.bookingRegistry = new HashMap<>();
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.waitlistManager = waitlistManager;
    }

    private int countActiveBookingsForUser(String userId) {
        int count = 0;
        for (Booking b : bookingRegistry.values()) {
            if (b.getUserId().equals(userId) && b.getBookingStatus() == BookingStatus.CONFIRMED) 
                count++;
        }

        return count;
    }

    public Booking getBooking(String bookingId) {
        return bookingRegistry.get(bookingId);
    }
    
    private int getConfirmedCountForEvent(String eventId) {
        int count = 0;
        for (Booking b : bookingRegistry.values()) {
            if (b.getEventId().equals(eventId) && b.getBookingStatus() == BookingStatus.CONFIRMED)
                count++;
            }

        return count;
    }
    public void addExistingBooking(Booking booking) {
        if (booking != null) {
            bookingRegistry.put(booking.getBookingId(), booking);
        }
    }

    public Collection<Booking> getAllBookings() {
        return bookingRegistry.values();
    }

    public Booking createBooking(String bookingId, String userId, String eventId, LocalDateTime createdAt) {
        User user = userManager.getUser(userId);
        Event event = eventManager.getEvent(eventId);
        
        if (user == null || event == null) {
            System.err.println("Booking failed: User or Event does not exist.");
            return null; 
        }
        
        if (event.getStatus() == EventStatus.CANCELLED) {
            System.err.println("Booking failed: Event is cancelled.");
            return null;
        }

        for (Booking b : bookingRegistry.values()) {
            if (b.getUserId().equals(userId) && b.getEventId().equals(eventId) && b.getBookingStatus() != BookingStatus.CANCELLED) {
                System.err.println("Booking failed: User is already booked or waitlisted for this event.");
                return null; 
            }
        }

        int currentActive = countActiveBookingsForUser(userId);
        if (currentActive >= user.getMaxBookings()) {
            System.err.println("Booking failed: User has reached their active booking limit.");
            return null;
        }

        int currentConfirmed = getConfirmedCountForEvent(eventId);
        BookingStatus assignedStatus;
        
        if (currentConfirmed < event.getCapacity())
            assignedStatus = BookingStatus.CONFIRMED;
        else
            assignedStatus = BookingStatus.WAITLISTED;

        Booking newBooking = new Booking(bookingId, userId, eventId, createdAt, assignedStatus);
        bookingRegistry.put(bookingId, newBooking);

        if (assignedStatus == BookingStatus.WAITLISTED) {
            waitlistManager.addToWaitlist(eventId, newBooking); 
        }

        return newBooking;
    }

    public boolean cancelBooking(String bookingId) {
        
        Booking bookingToCancel = bookingRegistry.get(bookingId);
        
        if (bookingToCancel == null || bookingToCancel.getBookingStatus() == BookingStatus.CANCELLED)
            return false;

        BookingStatus previousStatus = bookingToCancel.getBookingStatus();
        bookingToCancel.setBookingStatus(BookingStatus.CANCELLED);
        
        String eventId = bookingToCancel.getEventId();

        if (previousStatus == BookingStatus.WAITLISTED) 
            waitlistManager.removeFromWaitlist(eventId, bookingToCancel.getUserId());
        else if (previousStatus == BookingStatus.CONFIRMED) {
            Booking promotedBooking = waitlistManager.popNextFromWaitlist(eventId);
            
            if (promotedBooking != null) {
                promotedBooking.setBookingStatus(BookingStatus.CONFIRMED); 
                System.out.println("AUTOMATIC PROMOTION: User " + promotedBooking.getUserId() + 
                                   " is now CONFIRMED for Event " + eventId);
            }
        }

        return true;
    }
}

