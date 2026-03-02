package gui;

import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import manager.UserManager;
import model.users.*;

public class UserPane extends VBox {

    public UserPane(UserManager userManager) {

        TextField idField = new TextField();
        idField.setPromptText("User ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Student", "Staff", "Guest");

        Label result = new Label();

        Button addBtn = new Button("Add User");
        addBtn.setOnAction(e -> {
            try {
                User user = switch (typeBox.getValue()) {
                    case "Student" -> new Student(idField.getText(), nameField.getText(), emailField.getText());
                    case "Staff" -> new Staff(idField.getText(), nameField.getText(), emailField.getText());
                    case "Guest" -> new Guest(idField.getText(), nameField.getText(), emailField.getText());
                    default -> null;
                };

                if (user != null && userManager.addUser(user))
                    result.setText("User added.");
                else
                    result.setText("Failed.");
            } catch (Exception ex) {
                result.setText(ex.getMessage());
            }
        });

        setSpacing(10);
        getChildren().addAll(idField, nameField, emailField, typeBox, addBtn, result);
    }
}