package csc2b.client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ZEDEMClient extends Application
{
    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		//create the ClientPane, set up the Scene and Stage
        ZEDEMClientPane clientPane = new ZEDEMClientPane();

        Scene scene = new Scene(clientPane, 750, 550);
        primaryStage.setTitle("Audio File Transfer Application");
        primaryStage.setScene(scene);
        primaryStage.show();
				
	}
}
