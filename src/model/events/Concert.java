package model.events;

import model.enums.EventStatus;
import model.enums.EventType;
import java.time.LocalDateTime;
import validation.ValidationUtils;

public final class Concert extends Event {
    private String ageRestriction;

    public Concert(String eventId, String title, LocalDateTime dateTime, String location, int capacity, EventStatus status, String ageRestriction) {
        super(eventId, title, dateTime, location, capacity, status);
        this.ageRestriction = ageRestriction;
    }

    public String getAgeRestriction() { 
        return ageRestriction; 
    }

    @Override
    public EventType getEventType() {
        return EventType.CONCERT;
    }

    // --- NEW: Subclass Validation ---
    @Override
    public void validate() {
        // 1. Run the base Event class validation first
        super.validate(); 
        
        // 2. Run the Concert-specific validation
        ValidationUtils.requireNonBlank(ageRestriction, "Age restriction is required for Concerts.");
    }
}