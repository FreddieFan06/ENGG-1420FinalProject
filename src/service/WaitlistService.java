package service;

import model.bookings.Booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WaitlistService {
    
    // Outer Map is thread-safe for handling multiple events simultaneously.
    // Inner Map acts as our O(1) Queue: Key = userId, Value = Booking.
    private final Map<String, Map<String, Booking>> waitlists;

    public WaitlistService() {
        this.waitlists = new ConcurrentHashMap<>();
    }
    
    private Map<String, Booking> getEventWaitlist(String eventId) {
        return waitlists.computeIfAbsent(eventId, k -> Collections.synchronizedMap(new LinkedHashMap<>()));
    }

    public boolean addToWaitlist(String eventId, Booking booking) {
        if (eventId == null || booking == null || booking.getUserId() == null) {
            return false;
        }

        Map<String, Booking> queue = getEventWaitlist(eventId);
        
        // putIfAbsent is O(1) and thread-safe. 
        // It returns null if the user was successfully added (meaning they weren't already there).
        return queue.putIfAbsent(booking.getUserId(), booking) == null;
    }

    public Booking popNextFromWaitlist(String eventId) {
        Map<String, Booking> queue = waitlists.get(eventId);
        if (queue == null || queue.isEmpty()) {
            return null;
        }

        // We must synchronize explicitly when using an Iterator on a synchronized collection
        synchronized (queue) {
            if (!queue.isEmpty()) {
                Iterator<Map.Entry<String, Booking>> iterator = queue.entrySet().iterator();
                Map.Entry<String, Booking> firstUser = iterator.next(); // Get the oldest entry
                iterator.remove(); // O(1) removal
                return firstUser.getValue();
            }
        }
        return null;
    }

    public boolean removeFromWaitlist(String eventId, String userId) {
        Map<String, Booking> queue = waitlists.get(eventId);
        if (queue == null) {
            return false;
        }

        // O(1) removal by key, rather than looping through a list
        return queue.remove(userId) != null;
    }

    public List<Booking> getWaitlist(String eventId) {
        Map<String, Booking> queue = waitlists.get(eventId);
        if (queue == null) {
            return new ArrayList<>();
        }

        // Must synchronize when iterating/copying the synchronized map
        synchronized (queue) {
            return new ArrayList<>(queue.values());
        }
    }

    public void clearWaitlist(String eventId) {
        Map<String, Booking> queue = waitlists.get(eventId);
        if (queue != null) {
            queue.clear();
        }
    }
}