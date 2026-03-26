package main; 

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import gui.*;
import service.*;



public class Main extends Application {

        private UserService userService;
        private EventService eventService;
        private WaitlistService waitlistService;
        private BookingService bookingService;

        @Override
        public void start(Stage primaryStage) {

                // Initialize Managers
                userService = new UserService();
                eventService = new EventService();
                waitlistService = new WaitlistService();
                bookingService = new BookingService(
                        userService,
                        eventService,
                        waitlistService);

                // Setup Tabs
                TabPane root = new TabPane();

                root.getTabs().add(new Tab("Users",
                                new UserPane(userService)));

                root.getTabs().add(new Tab("Events",
                                new EventPane(eventService)));

                root.getTabs().add(new Tab("Bookings",
                                new BookingPane(userService, eventService, bookingService)));

        
                Scene scene = new Scene(root, 700, 500);
                primaryStage.setTitle("Campus Event Booking System");
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        // You don't need a main method here because Launcher.java handles it
}
