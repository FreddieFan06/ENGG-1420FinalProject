package model.users;

import exception.ValidationException;
import model.enums.UserType;
import validation.Validatable;
import validation.ValidationUtils;

public abstract class User implements Validatable {
    private String userId;
    private String name;
    private String email;
    private UserType userType;

    public User() {}

    public User(String userId, String name, String email, UserType userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserType getUserType() { return userType; }

    public abstract int getMaxBookings();

    // Self-validation method
    @Override
    public void validate() {
        // Check required fields
        ValidationUtils.requireNonBlank(userId, "User ID is required.");
        ValidationUtils.requireNonBlank(name, "User name is required.");
        ValidationUtils.requireNonBlank(email, "Email is required.");

        // Check email format
        if (!ValidationUtils.isValidEmail(email)) {
            throw new ValidationException("Invalid email format.");
        }

        // Check user type
        if (userType == null) {
            throw new ValidationException("User type is required.");
        }

        boolean validType = switch (userType) {
            case STUDENT, STAFF, GUEST -> true;
            default -> false;
        };

        if (!validType) {
            throw new ValidationException("User type must be STUDENT, STAFF, or GUEST.");
        }
    }
}