package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.enums.UserType;
import model.users.*;
import service.UserService;

public class RegisterPane extends StackPane {
    private final UserService userService;
    private final Runnable onBackToLogin;

    // UI Components made as fields to ensure the logic method can access them
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final ComboBox<UserType> typeBox = new ComboBox<>();

    public RegisterPane(UserService userService, Runnable onBackToLogin) {
        this.userService = userService;
        this.onBackToLogin = onBackToLogin;

        // Background styling matching your root CSS
        this.getStyleClass().add("root");

        // Central Registration Card
        VBox card = new VBox(20);
        card.getStyleClass().add("card"); // Uses your CSS drop-shadow and white BG
        card.setMaxWidth(450);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);

        Label title = new Label("Create Account");
        title.getStyleClass().add("page-header");

        // Setup Input Fields
        idField.setPromptText("User ID (e.g., U025)");
        nameField.setPromptText("Full Name");
        emailField.setPromptText("University Email");
        
        typeBox.getItems().addAll(UserType.values());
        typeBox.setPromptText("Select Your Role");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        // Buttons
        Button registerBtn = new Button("Register Now");
        registerBtn.getStyleClass().add("button-primary");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        
        // This explicitly calls a method that uses the fields
        registerBtn.setOnAction(e -> handleRegistration());

        Hyperlink backLink = new Hyperlink("Already have an account? Login here");
        backLink.setOnAction(e -> this.onBackToLogin.run());

        card.getChildren().addAll(title, idField, nameField, emailField, typeBox, registerBtn, backLink);
        this.getChildren().add(card);
    }

    /**
     * Logic for registration. 
     * Moving this to a separate method clears the "unused" warnings 
     * for userService and onBackToLogin.
     */
    private void handleRegistration() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        UserType type = typeBox.getValue();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || type == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Info", "Please fill in all fields.");
            return;
        }

        try {
            User newUser = switch (type) {
                case STUDENT -> new Student(id, name, email);
                case STAFF -> new Staff(id, name, email);
                case GUEST -> new Guest(id, name, email);
            };

            // Using the userService field
            userService.addUser(newUser);
            
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Account created. You can now login.");
            
            // Using the onBackToLogin field
            this.onBackToLogin.run(); 
            
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        // Apply CSS to the alert if the scene is available
        if (getScene() != null) {
            alert.getDialogPane().getStylesheets().addAll(getScene().getStylesheets());
        }
        
        alert.showAndWait();
    }
}