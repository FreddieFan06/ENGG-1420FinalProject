package manager;

import model.users.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class UserManager {
    private Map<String, User> usersRegistry;

    public UserManager() {
        this.usersRegistry = new HashMap<>();
    }

    public boolean addUser(User user) {
        if(user == null || usersRegistry.containsKey(user.getUserId()))
            return false;
        
        usersRegistry.put(user.getUserId(), user);
        return true;
    }

    public User getUser(String userId) {
        return usersRegistry.get(userId);
    }

    public Collection<User> getAllUsers() {
        return usersRegistry.values();
    }

    public int getUserCount() {
        return usersRegistry.size();
    }

    public boolean userExists(String userId) {
        return usersRegistry.containsKey(userId);
    }
}
