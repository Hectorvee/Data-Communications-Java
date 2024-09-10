import acsse.csc2b.leecher.LeecherMode;
import acsse.csc2b.seeder.SeederMode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * A class representing the main entry point of the application.
 * The Main class extends Application and provides a GUI for the user to select
 * between Leecher and Seeder modes.
 */
public class Main extends Application {
    private Button btnLeecherMode;
    private Button btnSeederMode;
    private GridPane gridPane;

    /**
     * The main entry point of the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the application and set up the GUI elements.
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        initializeGUI(primaryStage);

        setupEventHandlers();
    }

    /**
     * Set up event handlers for the GUI buttons.
     */
    private void setupEventHandlers() {
        // Set up event handlers
        btnLeecherMode.setOnAction(e -> {
            LeecherMode leecherMode = new LeecherMode();
            Scene leecherScene = new Scene(leecherMode, 550, 600);
            Stage leecherStage = new Stage();
            leecherStage.setScene(leecherScene);
            leecherStage.setTitle("Leecher");
            leecherStage.show();
        });

        btnSeederMode.setOnAction(e -> {
            SeederMode seederMode = new SeederMode();
            Scene seederScene = new Scene(seederMode, 550, 600);
            Stage seederStage = new Stage();
            seederStage.setScene(seederScene);
            seederStage.setTitle("Seeder");
            seederStage.show();
        });
    }

    /**
     * Initialize the GUI elements for the application.
     * @param primaryStage The primary stage of the application.
     */
    private void initializeGUI(Stage primaryStage) {
        // Set up GUI elements
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        btnSeederMode = new Button("Seeder Mode");
        btnLeecherMode = new Button("Leecher Mode");
        gridPane.add(btnSeederMode, 0, 0);
        gridPane.add(btnLeecherMode, 0, 1);

        Scene scene = new Scene(gridPane, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }
}
