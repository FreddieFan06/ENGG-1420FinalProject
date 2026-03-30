package model.users;

import java.util.ArrayList;

import exceptions.userexceptions.*;
import model.enums.UserType;
import model.events.Event;
import validation.Validatable;
import validation.ValidationUtils;

public abstract class User implements Validatable {
    private String userId;
    private String name;
    private String email;
    private ArrayList<Event> eventsCreated;
    private UserType userType;

    public User(String userId, String name, String email, UserType userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.eventsCreated = new ArrayList<>(); // IMPORTANT
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserType getUserType() { return userType; }

    public void addEvent(Event event) {
        if (event != null) {
            eventsCreated.add(event);
        }
    }

    public ArrayList<Event> getAllEvents() {
        return eventsCreated;
    }

    public abstract int getMaxBookings();

    @Override
    public void validate() {
        ValidationUtils.requireNonBlank(userId, "User ID is required.");
        ValidationUtils.requireNonBlank(name, "User name is required.");
        ValidationUtils.requireNonBlank(email, "Email is required.");

        if (!ValidationUtils.isValidEmail(email)) {
            throw new InvalidEmailException("Invalid email format.");
        }

        if (userType == null) {
            throw new UserTypeMissingException("User type is required.");
        }

        boolean validType = switch (userType) {
            case STUDENT, STAFF, GUEST -> true;
            default -> false;
        };

        if (!validType) {
            throw new InvalidUserTypeException("User type must be STUDENT, STAFF, or GUEST.");
        }
    }
}