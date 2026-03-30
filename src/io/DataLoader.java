package io;

import model.users.*;
import model.events.*;
import model.bookings.Booking;
import model.enums.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataLoader {

    /**
     * Helper to verify if the file exists before we try to read it.
     */
    private static File getValidatedFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            // This print statement is your best friend for debugging paths!
            System.err.println("❌ FILE MISSING: " + file.getAbsolutePath());
            throw new FileNotFoundException("Could not find CSV at: " + path);
        }
        return file;
    }

    public static Map<String, User> loadUsers(String path) throws IOException {
        Map<String, User> users = new HashMap<>();
        File file = getValidatedFile(path);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip Header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                
                String id = p[0].trim();
                String name = p[1].trim();
                String email = p[2].trim();
                String type = p[3].trim().toUpperCase();

                User u = switch (type) {
                    case "STAFF" -> new Staff(id, name, email);
                    case "GUEST" -> new Guest(id, name, email);
                    default -> new Student(id, name, email); // Default to Student
                };
                users.put(id, u);
            }
        }
        return users;
    }

    public static Map<String, Event> loadEvents(String path) throws IOException {
        Map<String, Event> events = new HashMap<>();
        File file = getValidatedFile(path);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip Header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);

                String id = p[0].trim();
                String title = p[1].trim();
                LocalDateTime dt = LocalDateTime.parse(p[2].trim());
                String loc = p[3].trim();
                int cap = Integer.parseInt(p[4].trim());
                EventStatus status = EventStatus.valueOf(p[5].trim().toUpperCase());
                String type = p[6].trim().toUpperCase();

                // Polymorphic creation based on Type column
                Event e = switch (type) {
                    case "WORKSHOP" -> new Workshop(id, title, dt, loc, cap, status, p[7].trim());
                    case "SEMINAR"  -> new Seminar(id, title, dt, loc, cap, status, p[8].trim());
                    case "CONCERT"  -> new Concert(id, title, dt, loc, cap, status, p[9].trim());
                    default -> null;
                };
                if (e != null) events.put(id, e);
            }
        }
        return events;
    }

    public static List<Booking> loadBookings(String path) throws IOException {
        List<Booking> list = new ArrayList<>();
        File file = getValidatedFile(path);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip Header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);

                list.add(new Booking(
                    p[0].trim(), // bookingId
                    p[1].trim(), // userId
                    p[2].trim(), // eventId
                    LocalDateTime.parse(p[3].trim()),
                    BookingStatus.valueOf(p[4].trim().toUpperCase())
                ));
            }
        }
        return list;
    }
}