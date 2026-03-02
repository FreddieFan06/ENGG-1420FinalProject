package gui;

import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import manager.EventManager;
import model.events.*;
import model.enums.EventStatus;
import java.time.LocalDateTime;

public class EventPane extends VBox {

    public EventPane(EventManager eventManager) {

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Workshop", "Seminar", "Concert");

        TextField extraField = new TextField();
        extraField.setPromptText("Topic / Speaker / Age");

        Label result = new Label();

        Button addBtn = new Button("Add Event");
        addBtn.setOnAction(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText());
                LocalDateTime now = LocalDateTime.now();

                Event event = switch (typeBox.getValue()) {
                    case "Workshop" -> new Workshop(idField.getText(), titleField.getText(), now,
                            locationField.getText(), capacity, EventStatus.ACTIVE, extraField.getText());

                    case "Seminar" -> new Seminar(idField.getText(), titleField.getText(), now,
                            locationField.getText(), capacity, EventStatus.ACTIVE, extraField.getText());

                    case "Concert" -> new Concert(idField.getText(), titleField.getText(), now,
                            locationField.getText(), capacity, EventStatus.ACTIVE, extraField.getText());

                    default -> null;
                };

                if (event != null && eventManager.addEvent(event))
                    result.setText("Event added.");
                else
                    result.setText("Failed.");

            } catch (Exception ex) {
                result.setText(ex.getMessage());
            }
        });

        setSpacing(10);
        getChildren().addAll(idField, titleField, locationField, capacityField,
                typeBox, extraField, addBtn, result);
    }
}
