package main; 

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import gui.*;
import manager.*;



public class Main extends Application {

        private UserManager userManager;
        private EventManager eventManager;
        private WaitlistManager waitlistManager;
        private BookingManager bookingManager;

        @Override
        public void start(Stage primaryStage) {

                // Initialize Managers
                userManager = new UserManager();
                eventManager = new EventManager();
                waitlistManager = new WaitlistManager();
                bookingManager = new BookingManager(
                                userManager,
                                eventManager,
                                waitlistManager);

                // Setup Tabs
                TabPane root = new TabPane();

                root.getTabs().add(new Tab("Users",
                                new UserPane(userManager)));

                root.getTabs().add(new Tab("Events",
                                new EventPane(eventManager)));

                root.getTabs().add(new Tab("Bookings",
                                new BookingPane(userManager, eventManager, bookingManager)));

        
                Scene scene = new Scene(root, 700, 500);
                primaryStage.setTitle("Campus Event Booking System");
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        // You don't need a main method here because Launcher.java handles it
}
