package manager;

import model.bookings.Booking;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class WaitlistManager {
    private Map<String, List<Booking>> waitlists;

    public WaitlistManager() {
        this.waitlists = new HashMap<>();
    }

    public List<Booking> getWaitlist(String eventId) {
        return waitlists.getOrDefault(eventId, new ArrayList<>());
    }

    public boolean addToWaitlist(String eventId, Booking booking) {
        if(eventId == null || booking == null)
            return false;

        waitlists.putIfAbsent(eventId, new ArrayList<>());
        List<Booking> list = waitlists.get(eventId);

        for(Booking b : list) {
            if(b.getUserId().equals(booking.getUserId()))
                return false;
        }
        list.add(booking);
        return true;
    }

    public List<Booking> getWaitlistQueue(String eventId) {
        return waitlists.getOrDefault(eventId, new ArrayList<>());
    }

    public boolean removeFromWaitlist(String eventId, String userId) {
        List<Booking> list = waitlists.get(eventId);
        if(list == null)
            return false;

        return list.removeIf(b -> b.getUserId().equals(userId));
    }

    public Booking popNextFromWaitlist(String eventId) {
        List<Booking> list = waitlists.get(eventId);

        if(list != null && !list.isEmpty())
            return list.remove(0);

        return null;
    }

    public void clearWaitlist(String eventId) {
        if(waitlists.containsKey(eventId))
            waitlists.get(eventId).clear();
    }
}