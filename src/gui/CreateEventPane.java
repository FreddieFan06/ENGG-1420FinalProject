package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.events.*;
import model.enums.*;
import service.EventService;
import java.time.LocalDateTime;

public class CreateEventPane extends VBox {

    // 1. The code goes inside this Constructor
    public CreateEventPane(EventService eventService) {
        // --- UI Setup ---
        setPadding(new Insets(40));
        setSpacing(20);
        getStyleClass().add("content-area");

        Label title = new Label("Publish New Event");
        title.getStyleClass().add("page-header");

        TextField nameField = new TextField(); 
        nameField.setPromptText("Event Name");
        
        TextField locField = new TextField(); 
        locField.setPromptText("Location");
        
        TextField capField = new TextField(); 
        capField.setPromptText("Capacity (e.g. 10)");
        
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Workshop", "Seminar", "Concert");
        typeBox.setPromptText("Select Event Type");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        Button createBtn = new Button("Add Event to Hub");
        createBtn.getStyleClass().add("button-primary");
        createBtn.setMaxWidth(Double.MAX_VALUE);

        // 2. The event handler logic starts here
        createBtn.setOnAction(e -> {
            try {
                // Logic to build the object
                String id = "E" + (eventService.getObservableEvents().size() + 101);
                int cap = Integer.parseInt(capField.getText());
                String type = typeBox.getValue();

                if (type == null) {
                    new Alert(Alert.AlertType.WARNING, "Please select an event type!").show();
                    return;
                }

                Event newEvent = switch (type) {
                    case "Seminar" -> new Seminar(id, nameField.getText(), LocalDateTime.now().plusDays(7), locField.getText(), cap, EventStatus.ACTIVE, "TBD");
                    case "Concert" -> new Concert(id, nameField.getText(), LocalDateTime.now().plusDays(10), locField.getText(), cap, EventStatus.ACTIVE, "All Ages");
                    default -> new Workshop(id, nameField.getText(), LocalDateTime.now().plusDays(5), locField.getText(), cap, EventStatus.ACTIVE, "General Topic");
                };

                // Saving to service
                eventService.addEvent(newEvent);

                // Success Feedback
                new Alert(Alert.AlertType.INFORMATION, "Event Published Successfully!").show();
                
                // Clear the form
                nameField.clear();
                locField.clear();
                capField.clear();
                typeBox.setValue(null);

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Capacity must be a valid number!").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });

        // 3. Add everything to the VBox
        getChildren().addAll(title, nameField, locField, capField, typeBox, createBtn);
    }
}