package service.security;

import model.events.Event;
import model.enums.UserType;
import model.users.User;
import model.bookings.Booking;

public class AccessControlService {

    // Staff can override booking limits
    public static boolean canOverrideUserBookingLimit(User user) {
        return user.getUserType() == UserType.STAFF;
    }

    // Check if the user owns the event
    public static boolean isEventOwner(Event event, User user) {
        for(Event e : user.getAllEvents()) {
            if (e.getEventId().equals(event.getEventId())) {
                return true;
            }
        }
        return false;
    }

    // Can view a booking: owner or staff (Fixed signature & removed UserService)
    public static boolean canViewBooking(User requestingUser, Booking booking) {
        if (canOverrideUserBookingLimit(requestingUser)) return true;
        
        return booking.getUserId().equals(requestingUser.getUserId());
    }

    // Can cancel a booking: owner or staff
    public static boolean canCancelBooking(User requestingUser, Booking booking) {
        return canOverrideUserBookingLimit(requestingUser)
                || requestingUser.getUserId().equals(booking.getUserId());
    }

    // Can create booking for a user: self or staff
    public static boolean canCreateBooking(User requestingUser, User targetUser) {
        return canOverrideUserBookingLimit(requestingUser)
                || requestingUser.getUserId().equals(targetUser.getUserId());
    }
}