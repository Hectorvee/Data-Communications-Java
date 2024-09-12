package csc2b.client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		BUKAClientPane clientPane = new BUKAClientPane();

        Scene scene = new Scene(clientPane, 750, 550);
        primaryStage.setTitle("BUKAH");
        primaryStage.setScene(scene);
        primaryStage.show();
				
	}
}
