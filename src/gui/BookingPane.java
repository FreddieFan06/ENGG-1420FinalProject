package gui;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import model.users.User;
import service.*;

public class BookingPane extends VBox {
    public BookingPane(User currentUser, UserService us, EventService es, BookingService bs) {
        getStyleClass().add("content-area");
        setSpacing(30);
        
        // 1. Center the entire page content horizontally
        this.setAlignment(Pos.TOP_CENTER); 

        // Header Section
        Label title = new Label("Event Bookings");
        title.getStyleClass().add("page-header");
        
        Label subTitle = new Label("Manage reservations and waitlists for campus events.");
        subTitle.setStyle("-fx-text-fill: #64748B;");

        VBox header = new VBox(5, title, subTitle);
        header.setAlignment(Pos.CENTER); // Center text in the header

        // Action Card
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(600);
        
        // 2. Align card internal elements to the center
        card.setAlignment(Pos.CENTER); 

        TextField eventIdField = new TextField();
        eventIdField.setPromptText("Enter Event ID (e.g., CONC-101)");
        eventIdField.setMaxWidth(400); // Prevent the field from being too wide
        
        Button bookBtn = new Button("Confirm Booking");
        bookBtn.getStyleClass().add("button-primary");

        card.getChildren().addAll(new Label("Quick Reserve"), eventIdField, bookBtn);

        getChildren().addAll(header, card);
    }
}