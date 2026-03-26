package gui;

import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import service.*;
import model.bookings.Booking;
import java.time.LocalDateTime;

public class BookingPane extends VBox {

    public BookingPane(UserService userService,
                       EventService eventService,
                       BookingService bookingService) {

        TextField bookingIdField = new TextField();
        bookingIdField.setPromptText("Booking ID");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");

        TextField eventIdField = new TextField();
        eventIdField.setPromptText("Event ID");

        Label result = new Label();

        Button createBtn = new Button("Create Booking");
        createBtn.setOnAction(e -> {
            Booking b = bookingService.createBooking(
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
            boolean ok = bookingService.cancelBooking(cancelField.getText());
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
