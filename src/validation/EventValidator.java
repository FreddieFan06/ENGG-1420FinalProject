package validation;

import exception.ValidationException;
import model.events.Concert;
import model.events.Event;
import model.events.Seminar;
import model.events.Workshop;
import model.enums.EventStatus;

public class EventValidator {

    public void validateAddEvent(Event event) {

        if (event == null) {
            throw new ValidationException("Event cannot be null.");
        }

        if (event.getEventId() == null || event.getEventId().isBlank()) {
            throw new ValidationException("Event ID is required.");
        }

        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new ValidationException("Event title is required.");
        }

        if (event.getCapacity() <= 0) {
            throw new ValidationException("Event capacity must be greater than 0.");
        }

        if (event.getStatus() == null) {
            throw new ValidationException("Event status is required.");
        }

        if (event.getStatus() != EventStatus.ACTIVE && event.getStatus() != EventStatus.CANCELLED) {
            throw new ValidationException("Event status must be ACTIVE or CANCELLED.");
        }

        if (event instanceof Workshop workshop) {
            if (workshop.getTopic() == null || workshop.getTopic().isBlank()) {
                throw new ValidationException("Workshop must have a topic.");
            }
        } else if (event instanceof Seminar seminar) {
            if (seminar.getSpeakerName() == null || seminar.getSpeakerName().isBlank()) {
                throw new ValidationException("Seminar must have a speaker name.");
            }
        } else if (event instanceof Concert concert) {
            if (concert.getAgeRestriction() == null || concert.getAgeRestriction().isBlank()) {
                throw new ValidationException("Concert must have an age restriction.");
            }
        } else {
            throw new ValidationException("Unknown event type.");
        }
    }
}