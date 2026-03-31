package service.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.bookings.Booking;
import model.enums.BookingStatus;
import model.events.Event;
import service.BookingService;
import service.EventService;

public class AnalyticsService {

    private BookingService bookingService;
    private EventService eventService;

    public AnalyticsService(BookingService bookingService, EventService eventService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
    }

    // Get all bookings for a specific event

    private List<Booking> getBookingsForEvent(String eventId) {
        Collection<Booking> allBookings = bookingService.getConfirmedAttendees(eventId);
        List<Booking> eventBookings = new ArrayList<>();
        List<Booking> tempList = new ArrayList<>(allBookings);

        for (int i = 0; i < tempList.size(); i++) {
            Booking booking = tempList.get(i);

            if (booking.getEventId().equals(eventId)) {
                eventBookings.add(booking);
            }
        }

        return eventBookings;
    }

    /*
     * Metric 1: Occupancy Rate
     * confirmed bookings / capacity
     */
    public double calculateOccupancyRate(String eventId) {
        Event event = eventService.getEvent(eventId);

        if (event == null || event.getCapacity() <= 0) {
            return 0.0;
        }

        List<Booking> bookings = getBookingsForEvent(eventId);

        int confirmed = 0;

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                confirmed++;
            }
        }

        return (double) confirmed / event.getCapacity();
    }

    /*
     * Metric 2: Cancellation Rate
     * cancelled bookings / total bookings
     */
    public double calculateCancellationRate(String eventId) {
        List<Booking> bookings = getBookingsForEvent(eventId);

        if (bookings.isEmpty()) {
            return 0.0;
        }

        int cancelled = 0;

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                cancelled++;
            }
        }

        return (double) cancelled / bookings.size();
    }

    /*
     * Metric 3: Waitlist to Confirmed Conversion Rate
     * confirmed bookings that were once waitlisted / total waitlisted bookings
     */
    public double calculateWaitlistConversionRate(String eventId) {
        List<Booking> bookings = getBookingsForEvent(eventId);

        if (bookings.isEmpty()) {
            return 0.0;
        }

        int everWaitlisted = 0;
        int convertedToConfirmed = 0;

        if (everWaitlisted == 0) {
            return 0.0;
        }

        return (double) convertedToConfirmed / everWaitlisted;
    }
}