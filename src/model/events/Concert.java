package model.events;

import model.enums.EventStatus;
import model.enums.EventType;
import java.time.LocalDateTime;

public class Concert extends Event {
    private String ageRestriction;

    public Concert(String eventId, String title, LocalDateTime dateTime, String location, int capacity, EventStatus status, String ageRestriction) {
        super(eventId, title, dateTime, location, capacity, status);
        this.ageRestriction = ageRestriction;
    }

    public String getAgeRestriction() { return ageRestriction; }

    @Override
    public EventType getEventType() {
        return EventType.CONCERT;
    }
}