package ui.booking;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/*

BookingWorkflowView

This class builds the GUI for the Booking Workflow.
Gives dropdowns for selecting users, events, and bookings, as
well as buttons for booking and cancelling.

*/

public class BookingWorkflowView {

    // Builds and returns the complete booking workflow layout.

    public static VBox build() {

        // Dropdown for selecting a user.

        ComboBox<String> userBox = new ComboBox<String>();
        userBox.getItems().addAll(
            "U001 - Example Student",
            "U002 - Example Staff",
            "U003 - Example Guest"
        );

        //Dropdown for selecting an event.

        ComboBox<String> eventBox = new ComboBox<String>();
        eventBox.getItems().addAll(
            "E101 - Example Workshop (Active)",
            "E205 - Example Seminar (Full)",
            "E330 - Example Concert (Cancelled)"
        );
        
        // Dropdown for selecting an existing booking.

        ComboBox<String> bookingBox = new ComboBox<String>();
        bookingBox.getItems().addAll(
            "B9001 - U001 - E101 (Confirmed)",
            "B9002 - U002 - E205 (Waitlisted)"
        );

        // Buttons for booking and cancelling.

        Button bookButton = new Button("Book Event");
        Button cancelButton = new Button("Cancel Booking");

        //Text area used to display status messages.

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false); // User cannot type in it
        outputArea.setPrefRowCount(8); // Preferred height

        // Placeholder booking logic.

        bookButton.setOnAction(e -> {
            String user = userBox.getValue();
            String event = eventBox.getValue();

            // Ensure selections were made.

            if (user == null || event == null) {
                outputArea.appendText("Select a user and an event first.\n");
                return;
            }

            // Prevent booking cancelled events

            if (event.contains("(Cancelled)")) {
                outputArea.appendText("Cannot book: event is Cancelled.\n");
                return;
            }

            // Simulate waitlist vs confirmed booking.

            if (event.contains("(Full)")) {
                outputArea.appendText("Booked (placeholder): WAITLISTED -> " + user + " -> " + event + "\n");
            } else {
                outputArea.appendText("Booked (placeholder): CONFIRMED -> " + user + " -> " + event + "\n");
            }
        });


        // Placeholder cancellation logic

        cancelButton.setOnAction(e -> {
            String booking = bookingBox.getValue();

            if (booking == null) {
                outputArea.appendText("Select a booking to cancel first.\n");
                return;
            }

            outputArea.appendText("Cancelled (placeholder): " + booking + "\n");

            // Simulate promotion of a waitlisted booking.

            if (booking.contains("(Confirmed)")) {
                outputArea.appendText("Promotion occurred (placeholder): first waitlisted booking promoted.\n");
            }
        });

        //Layout container (vertical stacking).

        VBox root = new VBox();
        root.setSpacing(10);

        // Add all UI components to the layout.

        root.getChildren().addAll(
            new Label("Booking Workflow"),
            new Label("User:"),
            userBox,
            new Label("Event:"),
            eventBox,
            bookButton,
            new Label("Booking:"),
            bookingBox,
            cancelButton,
            new Label("Output:"),
            outputArea
        );

        return root;
    }
}