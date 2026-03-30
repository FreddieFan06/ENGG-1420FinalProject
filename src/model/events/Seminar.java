package model.events;

import model.enums.EventStatus;
import model.enums.EventType;
import java.time.LocalDateTime;
import validation.ValidationUtils; 

public final class Seminar extends Event {
    private String speakerName;

    public Seminar(String eventId, String title, LocalDateTime dateTime, String location, int capacity, EventStatus status, String speakerName) {
        super(eventId, title, dateTime, location, capacity, status);
        this.speakerName = speakerName;
    }

    public String getSpeakerName() {
         return speakerName; 
    }

    @Override
    public EventType getEventType() {
        return EventType.SEMINAR;
    }

    // --- NEW: Subclass Validation ---
    @Override
    public void validate() {
        super.validate(); 
        ValidationUtils.requireNonBlank(speakerName, "Speaker name is required for Seminars.");
    }
}