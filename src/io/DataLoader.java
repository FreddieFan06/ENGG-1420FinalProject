package io;

import model.users.*;
import model.events.*;
import model.bookings.Booking;
import model.enums.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class DataLoader {
    public static Map<String, User> loadUsers(String path) throws IOException {
        Map<String, User> users = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = CSVUtils.parseLine(line);
                if (p.length < 4)
                     continue;

                String id = p[0].trim();
                String name = p[1].trim();
                String email = p[2].trim();
                String type = p[3].trim().toUpperCase();

                switch (type) {
                    case "STUDENT": 
                        users.put(id, new Student(id, name, email)); 
                        break;
                    case "STAFF": 
                        users.put(id, new Staff(id, name, email)); 
                        break;
                    case "GUEST": 
                        users.put(id, new Guest(id, name, email)); 
                        break;
                }
            }
        }
        return users;
    }

    public static Map<String, Event> loadEvents(String path) throws IOException {
        Map<String, Event> events = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = CSVUtils.parseLine(line);
                if (p.length < 10) continue; 

                String id = p[0].trim();
                String title = p[1].trim();
                LocalDateTime dateTime = LocalDateTime.parse(p[2].trim());
                String location = p[3].trim();
                int capacity = Integer.parseInt(p[4].trim());
                EventStatus status = EventStatus.valueOf(p[5].trim().toUpperCase());
                String eventType = p[6].trim().toUpperCase();
                
                String topic = p[7].trim();
                String speakerName = p[8].trim();
                String ageRestriction = p[9].trim();

                switch (eventType) {
                    case "WORKSHOP": 
                        events.put(id, new Workshop(id, title, dateTime, location, capacity, status, topic)); 
                        break;
                    case "SEMINAR": 
                        events.put(id, new Seminar(id, title, dateTime, location, capacity, status, speakerName)); 
                        break;
                    case "CONCERT": 
                        events.put(id, new Concert(id, title, dateTime, location, capacity, status, ageRestriction)); 
                        break;
                }
            }
        }
        return events;
    }

    public static List<Booking> loadBookings(String path) throws IOException {
        List<Booking> bookings = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = CSVUtils.parseLine(line);
                if (p.length < 5) continue;

                String bookingId = p[0].trim();
                String userId = p[1].trim();
                String eventId = p[2].trim();
                LocalDateTime createdAt = LocalDateTime.parse(p[3].trim());
                BookingStatus status = BookingStatus.valueOf(p[4].trim().toUpperCase());

                bookings.add(new Booking(bookingId, userId, eventId, createdAt, status));
            }
        }
        return bookings;
    }
}