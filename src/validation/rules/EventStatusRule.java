// Created by Shivom Bhanot (1319202)

package validation.rules;

import exceptions.eventexceptions.*;
import model.enums.EventStatus;

// Business rule that ensures an event is not cancelled before booking
public class EventStatusRule implements ValidationRule<BookingContext> {

    @Override
    public void validate(BookingContext context) {

        // IF the event status is cancelled, throw an exception
        if (context.getEvent().getStatus() == EventStatus.CANCELLED) {
            throw new EventCancelledException("Cannot book a cancelled event.");
        }
    }
}