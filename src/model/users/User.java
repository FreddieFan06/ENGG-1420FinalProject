package model.users;

import model.enums.UserType;

public abstract class User {
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

     public UserType getUserType() {
        return userType;
     }

    public abstract int getMaxBookings();
}