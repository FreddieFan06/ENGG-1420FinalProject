package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import service.UserService;
import model.enums.UserType;

public class UserPane extends VBox {

    public UserPane(UserService userService) {
        setPadding(new Insets(30));
        setSpacing(25);
        setAlignment(Pos.TOP_LEFT);

        // Page Title
        Label pageTitle = new Label("User Directory");
        pageTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // --- CARD: REGISTER USER ---
        VBox createCard = new VBox(15);
        createCard.getStyleClass().add("card");
        createCard.setMaxWidth(600);

        Label createHeader = new Label("Register New User");
        createHeader.getStyleClass().add("header-text");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        TextField userIdField = new TextField();
        TextField nameField = new TextField();
        ComboBox<UserType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(UserType.values()); // Populates from your enum
        typeBox.setPromptText("Select Role...");

        form.add(new Label("User ID:"), 0, 0);
        form.add(userIdField, 1, 0);
        form.add(new Label("Full Name:"), 0, 1);
        form.add(nameField, 1, 1);
        form.add(new Label("Role:"), 0, 2);
        form.add(typeBox, 1, 2);

        Button registerBtn = new Button("Register User");
        registerBtn.getStyleClass().add("primary-button");

        createCard.getChildren().addAll(createHeader, form, registerBtn);

        // Add everything to the page
        getChildren().addAll(pageTitle, createCard);
    }
}