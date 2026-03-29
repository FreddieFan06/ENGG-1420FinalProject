// Created by Shivom Bhanot (1319202)

package validation.rules;

import exceptions.eventexceptions.*;

// Business rule that ensures an event has not exceeded its capacity
public class EventCapacityRule implements ValidationRule<BookingContext> {

    @Override
    public void validate(BookingContext context) {

        // If the number of confirmed bookings is greater than or equal to event
        // capacity, throw exception
        if (context.getConfirmedCount() >= context.getEvent().getCapacity()) {
            throw new EventFullException("Event has reached full capacity.");
        }
    }
}