import acsse.csc2a.gui.ClientPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The entry point of the JavaFX application.
 * Extends the Application class to provide a JavaFX application lifecycle.
 */
public class Main extends Application {

    /**
     * Main method to launch the JavaFX application.
     * @param args Command line arguments (not used in this program).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is the main entry point for all JavaFX applications.
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage){
        // Create an instance of the main client pane
        ClientPane clientPane = new ClientPane();

        Scene scene = new Scene(clientPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }
}
