package model.events;

import java.time.LocalDateTime;

import exceptions.eventexceptions.InvalidEventCapacityException;
import exceptions.eventexceptions.InvalidEventStatusException;
import model.enums.EventStatus;
import model.enums.EventType;
import validation.Validatable;
import validation.ValidationUtils;

public abstract class Event implements Validatable {
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

    // Self-validation method
    @Override
    public void validate() {
        ValidationUtils.requireNonBlank(eventId, "Event ID is required.");
        ValidationUtils.requireNonBlank(title, "Event title is required.");
        ValidationUtils.requireNonBlank(location, "Event location is required.");
        ValidationUtils.requireNonNull(dateTime, "Event date and time is required.");
        if (capacity <= 0) throw new InvalidEventCapacityException("Event capacity must be greater than 0.");
        ValidationUtils.requireNonNull(status, "Event status is required.");

        if (status != EventStatus.ACTIVE && status != EventStatus.CANCELLED) {
            throw new InvalidEventStatusException("Event status must be ACTIVE or CANCELLED.");
        }
    }
}