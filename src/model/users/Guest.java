package model.users;

import model.enums.UserType;

public final class Guest extends User {
    private static final int MAX_BOOKINGS = 1;

    public Guest(String userId, String name, String email) {
        super(userId, name, email, UserType.GUEST);
    }

    @Override
    public int getMaxBookings() {
        return MAX_BOOKINGS ;
    }

    @Override
    public UserType getUserType() {
        return UserType.GUEST;
    }
}