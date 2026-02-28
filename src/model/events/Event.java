package model.events;
import model.enums.EventStatus;
import model.enums.EventType;
import java.time.LocalDateTime;

public abstract class Event {
    private String eventId;
    private String title;
    private LocalDateTime dateTime;
    private String location;
    private int capacity;
    private EventStatus status;

    public Event(String eventId, String title, LocalDateTime dateTime, String location, int capacity, EventStatus status) {
        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        setCapacity(capacity);
        this.status = status;
    }

    public String getEventId() { return eventId; }
    public String getTitle() { return title; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public EventStatus getStatus() { return status; }

    public void setStatus(EventStatus status) { 
        this.status = status; 
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) 
            throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity = capacity;
    }

    public abstract EventType getEventType();
}