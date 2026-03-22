package model.users;

import model.enums.UserType;

public final class Staff extends User {
    private static final int MAX_BOOKINGS = 5;

    public Staff(String userId, String name, String email) {
        super(userId, name, email, UserType.STAFF);
    }

    @Override
    public int getMaxBookings() {
        return MAX_BOOKINGS;
    }

    @Override
    public UserType getUserType() {
        return UserType.STAFF;
    }
}