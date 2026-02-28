package model.users;

import model.enums.UserType;

public class Guest extends User {
    public Guest(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxBookings() {
        return 1;
    }

    @Override
    public UserType getUserType() {
        return UserType.GUEST;
    }
}