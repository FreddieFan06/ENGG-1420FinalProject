package model.users;

import model.enums.UserType;

public class Student extends User {
    public Student(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxBookings() {
        return 3; 
    }

    @Override
    public UserType getUserType() {
        return UserType.STUDENT;
    }
}