package model.users;

import model.enums.UserType;

public abstract class User {
    private String userId;
    private String name;
    private String email;

    public User() {}

    public User(String userId, String name, String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    private boolean isValidEmail(String email) {
        return (email != null) && (email.matches("^[A-Za-z0-9._+-]+@(.+)$"));
    }

    public abstract int getMaxBookings();
    public abstract UserType getUserType();
}