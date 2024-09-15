package csc2b.client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Client class serves as the entry point for the JavaFX application.
 * It extends the Application class and initializes the main stage of the application.
 */
public class Client extends Application {

    /**
     * The main method is the entry point of the application.
     * It calls the launch method to start the JavaFX application lifecycle.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method initializes the main stage of the application.
     * It creates a new BUKAClientPane and sets up the scene for the stage.
     *
     * @param primaryStage The primary stage of the application.
     * @throws Exception If an exception occurs during the start process.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		BUKAClientPane clientPane = new BUKAClientPane();

        Scene scene = new Scene(clientPane, 750, 550);
        primaryStage.setTitle("BUKAH");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}
