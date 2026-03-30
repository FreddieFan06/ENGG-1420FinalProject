package service;

import model.users.User;
import java.util.HashMap;
import java.util.Map;

import exceptions.userexceptions.*;

import java.util.Collection;

import validation.ValidationUtils;

public class UserService {

    private Map<String, User> usersRegistry;

    public UserService() {
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

    public User login(String userId, String password) {
         User user = usersRegistry.get(userId);
         
         if (user != null && password.equals("1234")) 
            return user;
         return null;
    }

    public void registerUser(User newUser) {
        newUser.validate();
        if (usersRegistry.containsKey(newUser.getUserId()))
            throw new RuntimeException("User ID already exists!");
        
        usersRegistry.put(newUser.getUserId(), newUser);
    }

    public boolean authenticate(String userId, String providedPassword) {
        User user = usersRegistry.get(userId);
        if (user == null) return false;

        // Logic: Password is the part of the email BEFORE the @ sign
        String email = user.getEmail();
        if (email == null || !email.contains("@")) return false;
        
        String expectedPassword = email.split("@")[0];
        return expectedPassword.equals(providedPassword);
    }
}