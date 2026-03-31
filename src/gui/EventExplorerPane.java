package gui;

import javafx.collections.ListChangeListener;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.events.Event;
import service.*;

public class EventExplorerPane extends VBox {
    private final VBox cardsContainer = new VBox(20);
    private final EventService eventService;
    private final BookingService bookingService;
    private final TextField searchField = new TextField();

    public EventExplorerPane(EventService eventService, BookingService bookingService) {
        this.eventService = eventService;
        this.bookingService = bookingService;

        getStyleClass().add("content-area");
        setSpacing(20);
        setPadding(new Insets(40));

        // Header & Search Section
        Label header = new Label("Event Explorer");
        header.getStyleClass().add("page-header");

        searchField.setPromptText("🔍 Search events by title...");
        searchField.getStyleClass().add("text-field");
        searchField.setOnKeyReleased(e -> handleSearch());

        // Scrollable Container
        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Reactive listener: refresh cards on events change
        eventService.getObservableEvents().addListener((ListChangeListener<Event>) change -> handleSearch());

        // Initial load
        handleSearch();

        getChildren().addAll(header, searchField, scrollPane);
    }

    private void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        cardsContainer.getChildren().clear();

        eventService.getObservableEvents().stream()
                .filter(e -> e.getTitle().toLowerCase().contains(query))
                .forEach(e -> cardsContainer.getChildren().add(createEventCard(e)));
    }

    private HBox createEventCard(Event event) {
        HBox card = new HBox(20);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(5);
        // Display title AND Event ID
        Label t = new Label(event.getTitle() + "  [" + event.getEventId() + "]");
        t.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        String capInfo = bookingService.getAttendeeCount(event.getEventId()) + "/" + event.getCapacity();
        Label d = new Label(event.getEventType() + " | Capacity: " + capInfo);
        d.setStyle("-fx-text-fill: #64748B;");

        info.getChildren().addAll(t, d);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btn = new Button("Attendees");
        btn.getStyleClass().add("button-primary");
        btn.setOnAction(ev -> showAttendees(event));

        card.getChildren().addAll(info, spacer, btn);
        return card;
    }

    private void showAttendees(Event e) {
        Stage st = new Stage();
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));

        Label l1 = new Label("Confirmed Attendees:");
        l1.setStyle("-fx-font-weight: bold;");
        VBox confList = new VBox(5);
        bookingService.getConfirmedAttendees(e.getEventId())
                .forEach(b -> confList.getChildren().add(new Label("• " + b.getUserId())));

        Label l2 = new Label("Waitlist Queue:");
        l2.setStyle("-fx-font-weight: bold;");
        VBox waitList = new VBox(5);
        var wait = bookingService.getWaitlistedAttendees(e.getEventId());
        if (wait.isEmpty())
            waitList.getChildren().add(new Label("No one waiting."));
        else
            wait.forEach(b -> waitList.getChildren().add(new Label("• " + b.getUserId())));

        Button close = new Button("Close");
        close.getStyleClass().add("button-primary");
        close.setOnAction(ev -> st.close());

        root.getChildren().addAll(l1, confList, new Separator(), l2, waitList, close);

        Scene scene = new Scene(root, 350, 500);
        if (getScene() != null) {
            scene.getStylesheets().addAll(getScene().getStylesheets());
        }

        st.setScene(scene);
        // Popup title now includes Event ID
        st.setTitle("Attendees - " + e.getEventId() + " | " + e.getTitle());
        st.show();
    }
}