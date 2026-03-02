package gui;

import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import manager.*;
import model.bookings.Booking;
import java.time.LocalDateTime;

public class BookingPane extends VBox {

    public BookingPane(UserManager userManager,
            EventManager eventManager,
            BookingManager bookingManager) {

        TextField bookingIdField = new TextField();
        bookingIdField.setPromptText("Booking ID");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");

        TextField eventIdField = new TextField();
        eventIdField.setPromptText("Event ID");

        Label result = new Label();

        Button createBtn = new Button("Create Booking");
        createBtn.setOnAction(e -> {
            Booking b = bookingManager.createBooking(
                    bookingIdField.getText(),
                    userIdField.getText(),
                    eventIdField.getText(),
                    LocalDateTime.now());

            result.setText(b != null ? "Booking: " + b.getBookingStatus()
                    : "Booking failed");
        });

        TextField cancelField = new TextField();
        cancelField.setPromptText("Booking ID to cancel");

        Button cancelBtn = new Button("Cancel Booking");
        cancelBtn.setOnAction(e -> {
            boolean ok = bookingManager.cancelBooking(cancelField.getText());
            result.setText(ok ? "Cancelled" : "Cancel failed");
        });

        setSpacing(10);
        getChildren().addAll(
                bookingIdField, userIdField, eventIdField,
                createBtn,
                cancelField, cancelBtn,
                result);
    }
}
