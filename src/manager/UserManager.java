package manager;

import model.users.User;
import java.util.HashMap;
import java.util.Map;

import exceptions.userexceptions.*;

import java.util.Collection;

import validation.ValidationUtils;

public class UserManager {

    private Map<String, User> usersRegistry;

    public UserManager() {
        this.usersRegistry = new HashMap<>();
    }

    public boolean addUser(User user) {
        // --- START: OOP validation replacement ---
        ValidationUtils.requireNonNull(user, "User cannot be null");
        ValidationUtils.requireNonBlank(user.getUserId(), "User ID is required");
        ValidationUtils.requireNonBlank(user.getName(), "User name is required");
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            throw new InvalidEmailException("Invalid email format");
        }
        if (user.getUserType() == null) {
            throw new InvalidUserTypeException("User type is required");
        }
        // --- END: OOP validation replacement ---

        if (usersRegistry.containsKey(user.getUserId())) {
            throw new DuplicateUserIdException("User ID already exists.");
        }

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