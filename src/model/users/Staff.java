package model.users;

import model.enums.UserType;

public class Staff extends User {
    public Staff(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxBookings() {
        return 5;
    }

    @Override
    public UserType getUserType() {
        return UserType.STAFF;
    }
}