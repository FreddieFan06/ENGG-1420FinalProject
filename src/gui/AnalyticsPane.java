package gui;

import analytics.AnalyticsService;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.events.Event;
import service.EventService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnalyticsPane extends VBox {

    public AnalyticsPane(AnalyticsService analyticsService, EventService eventService) {
        setSpacing(10);

        Label titleLabel = new Label("Analytics");

        ComboBox<String> eventBox = new ComboBox<String>();
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(10);

        Button calculateButton = new Button("Calculate Metrics");

        Collection<Event> allEvents = eventService.getAllEvents();
        List<Event> eventList = new ArrayList<>(allEvents);

        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            eventBox.getItems().add(event.getEventId() + " - " + event.getTitle());
        }

        calculateButton.setOnAction(e -> {
            String selected = eventBox.getValue();

            if (selected == null) {
                outputArea.setText("Select an event first.");
                return;
            }

            String eventId = selected.split(" - ")[0];

            double occupancyRate = analyticsService.calculateOccupancyRate(eventId);
            double cancellationRate = analyticsService.calculateCancellationRate(eventId);
            double conversionRate = analyticsService.calculateWaitlistConversionRate(eventId);

            outputArea.setText(
                "Event: " + selected + "\n\n" +
                "Occupancy Rate: " + occupancyRate + "\n" +
                "Cancellation Rate: " + cancellationRate + "\n" +
                "Waitlist -> Confirmed Conversion Rate: " + conversionRate
            );
        });

        getChildren().addAll(
            titleLabel,
            new Label("Select Event:"),
            eventBox,
            calculateButton,
            new Label("Output:"),
            outputArea
        );
    }
}