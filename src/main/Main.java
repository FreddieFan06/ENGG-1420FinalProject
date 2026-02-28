package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("ENGG-1420 Final Project Beginning!");
        btn.setOnAction(e -> System.out.println("JavaFX is alive!"));
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.setTitle("Campus Event System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}