package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import gui.*;
import service.*;
import analytics.AnalyticsService;

public class Main extends Application {
    private Stage primaryStage;
    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;
    private WaitlistService waitlistService;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeBackend();
        showLoginView();
        primaryStage.setTitle("Campus Event Hub");
        primaryStage.show();
    }

    private void initializeBackend() {
        this.userService = new UserService();
        this.eventService = new EventService();
        this.waitlistService = new WaitlistService();
        // BookingService links the other three together
        this.bookingService = new BookingService(userService, eventService, waitlistService);

        private UserService userService;
        private EventService eventService;
        private WaitlistService waitlistService;
        private BookingService bookingService;
        private AnalyticsService analyticsService;

            DataLoader.loadUsers(baseDir + "users.csv").values().forEach(userService::addUser);
            DataLoader.loadEvents(baseDir + "events.csv").values().forEach(eventService::addEvent);
            DataLoader.loadBookings(baseDir + "bookings.csv").forEach(bookingService::addExistingBooking);
            
            System.out.println("✅ Data Persistence Active.");
        } catch (Exception e) {
            System.err.println("❌ CSV Load Failure: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

                // Initialize Managers
                userService = new UserService();
                eventService = new EventService();
                waitlistService = new WaitlistService();
                bookingService = new BookingService(
                        userService,
                        eventService,
                        waitlistService);
                analyticsService = new AnalyticsService(bookingService, eventService);

    public void showRegisterView() {
        updateRoot(new RegisterPane(userService, this::showLoginView));
    }

    public void showDashboardView(User user) {
        StackPane contentArea = new StackPane();
        SidebarNav sidebar = new SidebarNav(contentArea, user, this::showLoginView);

        // 1. Initialize the NEW reactive Explorer (Passes BOTH services)
        EventExplorerPane explorerPage = new EventExplorerPane(eventService, bookingService);
        
        // 2. Initialize other pages
        BookingPane bookingPage = new BookingPane(user, userService, eventService, bookingService);
        
        // 3. Setup Navigation
        sidebar.addNavigationItem("🗓", "All Events", explorerPage);
        sidebar.addNavigationItem("🎟", "My Schedule", bookingPage);

                root.getTabs().add(new Tab("Bookings",
                                new BookingPane(userService, eventService, bookingService)));
                                
                root.getTabs().add(new Tab("Analytics",
                                new AnalyticsPane(analyticsService, eventService)));

        updateRoot(shell);
    }

    private void updateRoot(javafx.scene.Parent root) {
        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(root, 1200, 800));
        } else {
            primaryStage.getScene().setRoot(root);
        }
        
        // Global CSS Injection
        try {
            primaryStage.getScene().getStylesheets().clear();
            var cssUrl = getClass().getResource("/css/style.css");
            if (cssUrl != null) {
                primaryStage.getScene().getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) { 
            System.err.println("❌ CSS Error: " + e.getMessage()); 
        }
    }

        // You don't need a main method here because Launcher.java handles it
}