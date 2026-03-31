package gui;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import service.*;
import model.events.Event;

public class EventPane extends VBox {
    private final EventService eventService;
    private final BookingService bookingService;
    private final VBox listContainer = new VBox(15);

    public EventPane(EventService es, BookingService bs) {
        this.eventService = es;
        this.bookingService = bs;
        
        getStyleClass().add("content-area");
        setSpacing(25);
        setPadding(new Insets(40));

        Label header = new Label("Event Explorer");
        header.getStyleClass().add("page-header");

        // 🚀 THE SCROLL FIX:
        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setFitToWidth(true); // Cards stretch to fill width
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal bar
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // Fill the remaining screen space

        refresh();
        getChildren().addAll(header, scrollPane);
    }

    public void refresh() {
        listContainer.getChildren().clear();
        for (Event e : eventService.getAllEvents()) {
            HBox card = new HBox(20);
            card.getStyleClass().add("card");
            card.setAlignment(Pos.CENTER_LEFT);

            VBox info = new VBox(5);
            Label t = new Label(e.getTitle());
            t.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            
            // Displays current attendee count vs capacity
            String capInfo = bookingService.getAttendeeCount(e.getEventId()) + "/" + e.getCapacity();
            Label d = new Label(e.getEventType() + " | Capacity: " + capInfo);
            d.setStyle("-fx-text-fill: #64748B;");
            
            info.getChildren().addAll(t, d);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button btn = new Button("Attendees");
            btn.getStyleClass().add("button-primary");
            btn.setOnAction(ev -> showAttendees(e));

            card.getChildren().addAll(info, spacer, btn);
            listContainer.getChildren().add(card);
        }
    }

    private void showAttendees(Event e) {
        Stage st = new Stage();
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        
        Label l1 = new Label("Confirmed Attendees:");
        l1.setStyle("-fx-font-weight: bold;");
        VBox confList = new VBox(5);
        bookingService.getConfirmedAttendees(e.getEventId()).forEach(b -> confList.getChildren().add(new Label("• " + b.getUserId())));

        Label l2 = new Label("Waitlist Queue:");
        l2.setStyle("-fx-font-weight: bold;");
        VBox waitList = new VBox(5);
        var wait = bookingService.getWaitlistedAttendees(e.getEventId());
        if (wait.isEmpty()) waitList.getChildren().add(new Label("No one waiting."));
        else wait.forEach(b -> waitList.getChildren().add(new Label("• " + b.getUserId())));

        Button close = new Button("Close");
        close.setOnAction(ev -> st.close());

        root.getChildren().addAll(l1, confList, new Separator(), l2, waitList, close);
        st.setScene(new Scene(root, 350, 500));
        st.show();
    }
}