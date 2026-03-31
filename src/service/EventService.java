package service;

import io.DataWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.*;
import exceptions.eventexceptions.*;
import model.enums.EventStatus;
import model.enums.EventType;
import model.events.Event;

public class EventService {
    // Standard Map for logic and fast O(1) lookups
    private Map<String, Event> eventsRegistry;
    private String eventCsvPath;
    private boolean persistEnabled;
    
    // The "Bridge" to the UI. Any ListView bound to this will auto-refresh.
    private final ObservableList<Event> observableEvents;

    public EventService() {
        this.persistEnabled = false;
        this.eventsRegistry = new HashMap<>();
        this.observableEvents = FXCollections.observableArrayList();
    }

    public void enablePersistence(String csvPath) {
        this.eventCsvPath = csvPath;
        this.persistEnabled = true;
    }

    private void saveEvents() {
        if (!persistEnabled || eventCsvPath == null || eventCsvPath.isBlank()) {
            return;
        }
        try {
            DataWriter.saveEvents(eventCsvPath, getAllEvents());
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist events: " + e.getMessage(), e);
        }
    }

    public boolean addEvent(Event event) {
        // --- Validation Logic ---
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

        if (eventsRegistry.containsKey(event.getEventId())) {
            throw new DuplicateEventIDException("Event ID already exists: " + event.getEventId());
        }

        // 1. Add to the Map (Logical Registry)
        eventsRegistry.put(event.getEventId(), event);
        
        // 2. Add to the ObservableList (UI Sync)
        observableEvents.add(event);

        saveEvents();
        
        return true;
    }

    /**
     * Used by UI components (like EventExplorer) to "listen" for changes.
     */
    public ObservableList<Event> getObservableEvents() {
        return observableEvents;
    }

    public Event getEvent(String eventId) {
        return eventsRegistry.get(eventId);
    }

    public Collection<Event> getAllEvents() {
        return eventsRegistry.values();
    }

    /**
     * Cancels an event and clears its waitlist.
     */
    public boolean cancelEvent(String eventId, WaitlistService waitlistService) {
        Event event = eventsRegistry.get(eventId);

        if (event != null && event.getStatus() != EventStatus.CANCELLED) {
            event.setStatus(EventStatus.CANCELLED);
            waitlistService.clearWaitlist(eventId);
            
            // Note: Since the object in the list is the same object in the map,
            // the UI will reflect the status change if you have a CellFactory set up.
            return true;
        }

        return false;
    }

    /**
     * Searches for events based on title and type.
     */
    public List<Event> searchEvents(String titleKeyWord, EventType type) {
        List<Event> results = new ArrayList<>();

        for (Event event : eventsRegistry.values()) {
            boolean matchesTitle = true;
            if (titleKeyWord != null && !titleKeyWord.trim().isEmpty()) {
                matchesTitle = event.getTitle().toLowerCase().contains(titleKeyWord.toLowerCase());
            }

            boolean matchesType = true;
            if (type != null) {
                matchesType = (event.getEventType() == type);
            }

            if (matchesTitle && matchesType) {
                results.add(event);
            }
        }

        return results;
    }
}