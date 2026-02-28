package model.events;

import model.enums.EventStatus;
import model.enums.EventType;
import java.time.LocalDateTime;

public class Workshop extends Event {
    private String topic;

    public Workshop(String eventId, String title, LocalDateTime dateTime, String location, int capacity, EventStatus status, String topic) {
        super(eventId, title, dateTime, location, capacity, status);
        this.topic = topic;
    }

    public String getTopic() { 
        return topic; 
    }

    @Override
    public EventType getEventType() {
        return EventType.WORKSHOP;
    }
}