package security;

import model.events.Event;
import model.enums.UserType;
import model.users.User;

public class AccessControlService {

    // canOverrideUserBookingLimit method takes a user as a parameter,
    // and checks whether or not that user is allowed to override
    // their booking limit based on the type of user they are
    // (i.e. Guest, Student, Staff).

    public static boolean canOverrideUserBookingLimit(User user) {
        if (user.getUserType() == UserType.STAFF)   // If the user is staff,
            return true;                            // they are allowed to.

        else                // Otherwise,
            return false;   // they are not allowed to.
    }


    // eventOwnership method takes an event and a user as parameters.
    // It will loop through the list of events created by that user,
    // and compare the parameter event's ID with the event ID
    // of each list item.

    public static boolean eventOwnership(Event event, User user) {
        for(Event e : user.getAllEvents()) {
            if (e.getEventId() == event.getEventId())   // If the IDs match,
                return true;                            // it will return true.
        }
        return false;   //Otherwise, it will return false.
    }

}
