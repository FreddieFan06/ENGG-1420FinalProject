package ui.booking;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class BookingWorkflowView {
    public static VBox build() {
        ComboBox<String> userBox = new ComboBox<String>();
        userBox.getItems().addAll(
            "U001 - Example Student",
            "U002 - Example Staff",
            "U003 - Example Guest"
        );

        ComboBox<String> eventBox = new ComboBox<String>();
        eventBox.getItems().addAll(
            "E101 - Example Workshop (Active)",
            "E205 - Example Seminar (Full)",
            "E330 - Example Concert (Cancelled)"
        );
        
        ComboBox<String> bookingBox = new ComboBox<String>();
        bookingBox.getItems().addAll(
            "B9001 - U001 - E101 (Confirmed)",
            "B9002 - U002 - E205 (Waitlisted)"
        );

        Button bookButton = new Button("Book Event");
        Button cancelButton = new Button("Cancel Booking");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(8);

        bookButton.setOnAction(e -> {
            String user = userBox.getValue();
            String event = eventBox.getValue();

            if (user == null || event == null) {
                outputArea.appendText("Select a user and an event first.\n");
                return;
            }

            if (event.contains("(Cancelled)")) {
                outputArea.appendText("Cannot book: event is Cancelled.\n");
                return;
            }

            if (event.contains("(Full)")) {
                outputArea.appendText("Booked (placeholder): WAITLISTED -> " + user + " -> " + event + "\n");
            } else {
                outputArea.appendText("Booked (placeholder): CONFIRMED -> " + user + " -> " + event + "\n");
            }
        });

        cancelButton.setOnAction(e -> {
            String booking = bookingBox.getValue();

            if (booking == null) {
                outputArea.appendText("Select a booking to cancel first.\n");
                return;
            }

            outputArea.appendText("Cancelled (placeholder): " + booking + "\n");

            if (booking.contains("(Confirmed)")) {
                outputArea.appendText("Promotion occurred (placeholder): first waitlisted booking promoted.\n");
            }
        });

        VBox root = new VBox();
        root.setSpacing(10);

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