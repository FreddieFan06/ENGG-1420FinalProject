package manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.eventexceptions.*;
import model.enums.EventStatus;
import model.enums.EventType;
import model.events.Event;

public class EventManager {
    private Map<String, Event> eventsRegistry;

    public EventManager() {
        this.eventsRegistry = new HashMap<>();
    }

    public boolean addEvent(Event event) {
        // --- START: OOP validation replacement ---
        // For now, we can still use ValidationUtils or create custom EventRules if
        // needed
        if (event == null)
            throw new EventNullException("Event cannot be null");
        if (event.getEventId() == null || event.getEventId().isBlank())
            throw new InvalidEventIDException("Event ID is required");
        if (event.getTitle() == null || event.getTitle().isBlank())
            throw new InvalidEventTitleException("Event title is required");
        if (event.getCapacity() <= 0)
            throw new InvalidEventCapacityException("Event capacity must be > 0");
        if (event.getStatus() == null)
            throw new InvalidEventStatusException("Event status is required");

        // --- END: OOP validation replacement ---

        if (eventsRegistry.containsKey(event.getEventId())) {
            throw new DuplicateEventIDException("Event ID already exists.");
        }

        eventsRegistry.put(event.getEventId(), event);
        return true;
    }

    public Event getEvent(String eventId) {
        return eventsRegistry.get(eventId);
    }

    public Collection<Event> getAllEvents() {
        return eventsRegistry.values();
    }

    public boolean cancelEvent(String eventId, WaitlistManager waitlistManager) {
        Event event = eventsRegistry.get(eventId);
        if (event != null && event.getStatus() != EventStatus.CANCELLED) {
            event.setStatus(EventStatus.CANCELLED);
            waitlistManager.clearWaitlist(eventId);
            return true;
        }

        return false;
    }

    public List<Event> searchEvents(String titleKeyWord, EventType type) {
        List<Event> results = new ArrayList<>();
        for (Event event : eventsRegistry.values()) {
            boolean matchesTitle = true;

            if (titleKeyWord != null && !titleKeyWord.trim().isEmpty())
                matchesTitle = event.getTitle().toLowerCase().contains(titleKeyWord.toLowerCase());

            boolean matchesType = true;
            if (type != null)
                matchesType = (event.getEventType() == type);

            if (matchesTitle && matchesType)
                results.add(event);
        }
        return results;
    }
}