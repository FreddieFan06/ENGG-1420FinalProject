package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import gui.*;
import service.*;
import io.DataLoader;
import model.users.User;
import java.io.File;

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

        try {
            String sep = File.separator;
            String baseDir = System.getProperty("user.dir") + sep + "src" + sep + "main" + sep + "resources" + sep
                    + "data" + sep;

            System.out.println("📂 Loading data from: " + baseDir);

            DataLoader.loadUsers(baseDir + "users.csv").values().forEach(userService::addUser);
            DataLoader.loadEvents(baseDir + "events.csv").values().forEach(eventService::addEvent);
            DataLoader.loadBookings(baseDir + "bookings.csv").forEach(bookingService::addExistingBooking);

            // Enable persistence once initial load is complete.
            userService.enablePersistence(baseDir + "users.csv");
            eventService.enablePersistence(baseDir + "events.csv");
            bookingService.enablePersistence(baseDir + "bookings.csv");

            System.out.println("✅ Data Persistence Active.");
        } catch (Exception e) {
            System.err.println("❌ CSV Load Failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        // Logic inside LoginPane will use userService and the switch-view runnables
        updateRoot(new LoginPane(userService, this::showDashboardView, this::showRegisterView));
    }

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

        // 4. Staff-only functionality
        if (user.getUserType() == model.enums.UserType.STAFF) {
            sidebar.addNavigationItem("➕", "Create Event", new CreateEventPane(eventService));
        }

        // 5. Construct the UI Shell
        BorderPane shell = new BorderPane();
        shell.getStyleClass().add("root"); // Applies global background from CSS
        shell.setLeft(sidebar);
        shell.setCenter(contentArea);

        // Default View
        contentArea.getChildren().add(explorerPage);

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

    public static void main(String[] args) {
        launch(args);
    }
}