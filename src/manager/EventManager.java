package manager;

import model.events.Event;
import model.enums.EventStatus;
import model.enums.EventType;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class EventManager {
    private Map<String, Event> eventsRegistry;

    public EventManager() {
        this.eventsRegistry = new HashMap<>();
    }

    public boolean addEvent(Event event) {
        if(event == null || eventsRegistry.containsKey(event.getEventId()))
            return false;

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
        if(event != null && event.getStatus() != EventStatus.CANCELLED) {
            event.setStatus(EventStatus.CANCELLED);
            waitlistManager.clearWaitlist(eventId);
            return true;
        }

        return false;
    }

    public List<Event> searchEvents(String titleKeyWord, EventType type) {
        List<Event> results = new ArrayList<>();
        for(Event event : eventsRegistry.values()) {
            boolean matchesTitle = true;

            if(titleKeyWord != null && !titleKeyWord.trim().isEmpty())
                matchesTitle = event.getTitle().toLowerCase().contains(titleKeyWord.toLowerCase());

            boolean matchesType = true;
            if(type != null)
                matchesType = (event.getEventType() == type);

            if(matchesTitle && matchesType)
                results.add(event);
        }
        return results;
    }
}
