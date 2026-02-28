package io;

import model.users.*;
import model.events.*;
import model.bookings.Booking;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class DataWriter {
    public static void saveUsers(String path, Collection<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("userId,name,email,userType"); // Header
            bw.newLine();
            for (User u : users) {
                bw.write(String.format("%s,%s,%s,%s", 
                    u.getUserId(), u.getName(), u.getEmail(), u.getUserType()));
                bw.newLine();
            }
        }
    }

    public static void saveEvents(String path, Collection<Event> events) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");
            bw.newLine();
            for (Event e : events) {
                String topic = (e instanceof Workshop) ? ((Workshop)e).getTopic() : "";
                String speaker = (e instanceof Seminar) ? ((Seminar)e).getSpeakerName() : "";
                String age = (e instanceof Concert) ? ((Concert)e).getAgeRestriction() : "";

                bw.write(String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s,%s",
                    e.getEventId(), e.getTitle(), e.getDateTime(), e.getLocation(), 
                    e.getCapacity(), e.getStatus(), e.getEventType(), topic, speaker, age));
                bw.newLine();
            }
        }
    }

    public static void saveBookings(String path, Collection<Booking> bookings) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("bookingId,userId,eventId,createdAt,bookingStatus");
            bw.newLine();
            for (Booking b : bookings) {
                bw.write(String.format("%s,%s,%s,%s,%s",
                    b.getBookingId(), b.getUserId(), b.getEventId(), b.getCreatedAt(), b.getBookingStatus()));
                bw.newLine();
            }
        }
    }
}