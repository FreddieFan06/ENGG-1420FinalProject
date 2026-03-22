package security;

import model.events.Event;
import model.enums.UserType;
import model.users.User;

public class AccessControlService {
    public static boolean canOverrideUserBookingLimit(User user) {
        if (user.getUserType() == UserType.STAFF)
            return true;
        else
            return false;
    }


    // Create a method that takes an event, and a user as parameters.
    // It will loop through the list of events created by that user,
    // and compare it to the eventID
    
    public static boolean eventOwnership(Event event, User user) {
        for(Event e : user.getAllEvents()) {
            if (e.getEventId() == event.getEventId())
                return true;
        }
        return false;
    }

}
