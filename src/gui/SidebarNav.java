package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.users.User;

public class SidebarNav extends VBox {
    private StackPane contentArea;
    private Runnable onLogout;

    public SidebarNav(StackPane contentArea, User user, Runnable onLogout) {
        this.contentArea = contentArea;
        this.onLogout = onLogout;

        getStyleClass().add("sidebar");
        setSpacing(10);
        setPadding(new Insets(30, 0, 20, 0));
        setMinWidth(250);

        Label logo = new Label("CAMPUS HUB");
        logo.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px;");
        logo.setPadding(new Insets(0, 0, 30, 25));
        getChildren().add(logo);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        // Footer Section
        VBox footer = new VBox(15);
        footer.setPadding(new Insets(20, 25, 20, 25));
        footer.setStyle("-fx-background-color: #1E293B;");
        
        HBox profileInfo = new HBox(10);
        profileInfo.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(12, Color.DODGERBLUE);
        VBox nameBox = new VBox(2);
        Label n = new Label(user.getName());
        n.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label r = new Label(user.getUserType().toString());
        r.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11px;");
        nameBox.getChildren().addAll(n, r);
        profileInfo.getChildren().addAll(dot, nameBox);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> this.onLogout.run());

        footer.getChildren().addAll(profileInfo, logoutBtn);
        getChildren().add(footer);
    }

    public void addNavigationItem(String icon, String label, Pane page) {
        Button b = new Button(icon + "  " + label);
        b.getStyleClass().add("nav-item");
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        
        b.setOnAction(event -> {
            // Clear current active styles from all buttons
            for (Node node : getChildren()) {
                if (node instanceof Button) node.getStyleClass().remove("nav-item-active");
            }
            // Highlight this button
            b.getStyleClass().add("nav-item-active");

            // Swap content
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);
        });

        // Insert above the spacer
        getChildren().add(getChildren().size() - 2, b);
    }
}