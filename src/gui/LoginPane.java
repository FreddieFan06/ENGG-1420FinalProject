package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.users.User;
import service.UserService;
import java.util.function.Consumer;

public class LoginPane extends StackPane {
    public LoginPane(UserService userService, Consumer<User> onLoginSuccess, Runnable onRegister) {
        // Main Container
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(400);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        Label title = new Label("Welcome Back");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");

        // Input Fields
        VBox idBox = new VBox(5);
        idBox.getChildren().addAll(new Label("User ID"), new TextField());
        TextField idField = (TextField) idBox.getChildren().get(1);
        idField.setPromptText("e.g. U001");

        VBox passBox = new VBox(5);
        // 🚀 Using PasswordField so characters are hidden
        PasswordField passField = new PasswordField();
        passField.setPromptText("Your unique password");
        passBox.getChildren().addAll(new Label("Password"), passField);

        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("button-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPadding(new Insets(12));

        Hyperlink regLink = new Hyperlink("Don't have an account? Create one");
        regLink.setOnAction(e -> onRegister.run());

        // Login Logic
        loginBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String password = passField.getText().trim();

            User user = userService.getUser(id);
            
            if (user != null) {
                // Check if password matches email prefix
                String emailPrefix = user.getEmail().split("@")[0];
                
                if (password.equals(emailPrefix)) {
                    onLoginSuccess.accept(user);
                } else {
                    showError("Invalid Password", "Hint: Your password is your email prefix (e.g. alice.smith)");
                }
            } else {
                showError("User Not Found", "The User ID " + id + " does not exist.");
            }
        });

        card.getChildren().addAll(title, idBox, passBox, loginBtn, regLink);
        this.getChildren().add(card);
        this.setStyle("-fx-background-color: #F8FAFC;"); // Light grey background
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}