package acsse.csc2b.client;

import acsse.csc2b.client.gui.ClientPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ImgClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ClientPane clientPane = new ClientPane();

        Scene scene = new Scene(clientPane, 550, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();

    }
}
