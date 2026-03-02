package main;

import gui.BookingPane;
import gui.EventPane;
import gui.UserPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import manager.BookingManager;
import manager.EventManager;
import manager.UserManager;
import manager.WaitlistManager;
import ui.booking.BookingWorkflowView;

public class Main extends Application {

    private UserManager userManager;
    private EventManager eventManager;
    private WaitlistManager waitlistManager;
    private BookingManager bookingManager;

    @Override
    public void start(Stage primaryStage) {

        userManager = new UserManager();
        eventManager = new EventManager();
        waitlistManager = new WaitlistManager();
        bookingManager = new BookingManager(
                userManager,
                eventManager,
                waitlistManager);

        TabPane root = new TabPane();

        root.getTabs().add(new Tab("Users",
                new UserPane(userManager)));

        root.getTabs().add(new Tab("Events",
                new EventPane(eventManager)));

        root.getTabs().add(new Tab("Bookings",
                new BookingPane(userManager, eventManager, bookingManager)));

        root.getTabs().add(new Tab("Booking Workflow",
                BookingWorkflowView.build()));

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Campus Event Booking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}