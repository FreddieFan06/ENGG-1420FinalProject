package security;

import model.enums.UserType;
import model.users.User;

public class AccessControlService {
    public AccessControlService() {

        /*if (UserType == STUDENT) {
            Set access to 'protected'
            Should only allow access to events
            from the user.
        } 
        
        if (UserType == STAFF) {
            Set access to 'public';
            Should allow access to all events.
        }
        
        if (UserType == GUEST) {
            Set access to 'private';
            shouldn't allow any edit access.
        }*/
    }

    public boolean canOverrideUserBookingLimit(User user) {
        if (user.getUserType() == UserType.STAFF) {
            return true;
        }
        else {
            return false;
        }
    }
}
