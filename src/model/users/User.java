package model.users;

import java.util.ArrayList;
import model.enums.UserType;
import model.events.Event;

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private ArrayList<Event> eventsCreated;

//    public User() {}

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }



    public void addEvent(Event event){
        if (event != null)
            eventsCreated.add(event);
    }


    public ArrayList<Event> getAllEvents(){
        return eventsCreated;
    }


    /*
        Need two extra methods:
        1. Method that adds an event to the eventsCreated ArrayList
        2. Method that returns all of the events in eventsCreated
    */

    public abstract int getMaxBookings();
    public abstract UserType getUserType();
}