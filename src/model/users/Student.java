package model.users;

import model.enums.UserType;

public final class Student extends User {
    private static final int MAX_BOOKINGS = 3;

    public Student(String userId, String name, String email) {
        super(userId, name, email, UserType.STUDENT);
    }

    @Override
    public int getMaxBookings() {
        return MAX_BOOKINGS;
    }
}