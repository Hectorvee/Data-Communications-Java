package acsse.csc2b.client.gui;

import acsse.csc2b.client.handler.ClientHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientPane extends GridPane {

    private Label idLabel;
    private Label serverResponseLabel;
    private Label imageLabel;
    private Label fileListLabel;
    private TextField downloadField;
    private TextArea serverResponseArea;
    private TextArea fileListArea;
    private Button btnDownload;
    private Button btnPull;
    private Button btnUpload;
    private ClientHandler clientHandler;
    private ImageView imageView;
    private String[] imageList;


    public ClientPane() {
        clientHandler = new ClientHandler();
        idLabel = new Label("Enter file ID to retrieve: ");
        serverResponseLabel = new Label("Server Response: ");
        fileListLabel = new Label("File List: ");
        downloadField = new TextField();
        serverResponseArea = new TextArea();
        fileListArea = new TextArea();
        btnDownload = new Button("Download");
        btnPull = new Button("PULL");
        btnUpload = new Button("UPLOAD");
        imageView = new ImageView();
        imageLabel = new Label("Image: ");
        initGUI();
    }

    private void initGUI() {

        // Set action for the Send button
        btnDownload.setOnAction(e -> {
            handleDownload();
            downloadField.clear();
        });

        // Set action for the Pull button
        btnPull.setOnAction(e -> {
            handleList();
        });

        // Set action for the Upload button
        btnUpload.setOnAction(e -> {
            handleUpload();
        });

        // Add components to the clientGrid
        this.add(btnPull, 0, 0);
        this.add(fileListLabel, 0, 1);
        this.add(fileListArea, 0, 2, 3, 1);
        this.add(idLabel, 0, 3);
        this.add(downloadField, 1, 3);
        this.add(btnDownload, 2, 3);
        this.add(btnUpload, 0, 4);
        this.add(serverResponseLabel, 0, 5);
        this.add(new ScrollPane(serverResponseArea), 0, 6, 3, 1);
        this.add(imageLabel, 0, 7); // Add ImageView to the layout
        this.add(imageView, 0, 8, 3, 1); // Add ImageView to the layout

        // Set up the grid
        setUpGrid();

    }

    private void setUpGrid() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(20, 20, 20, 20)); // Top, right, bottom, left padding
        this.setVgap(10);  // Vertical spacing between rows
        this.setHgap(10);  // Horizontal spacing between columns
    }

    private void displayImage(String imagePath) {
        try {
            Image image = new Image(imagePath);
            imageView.setImage(image);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
        } catch (IllegalArgumentException e) {
            DialogGUI.errorDialog("Image Load Error", "Invalid Image Path", "The specified image could not be loaded.");
        }
    }

    private void handleUpload() {
        // Code for handling
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String filename = selectedFile.getName();
            int fileID = imageList.length + 1;
            String command = "UP " + fileID + " " + filename + " " + selectedFile.length();
            String response;
            try {
                response = clientHandler.uploadCommand(selectedFile, command);
            } catch (IOException e) {
                response = "Error: " + e.getMessage();
            }
            serverResponseArea.appendText("Command: " + command + "\nSERVER> " + response + "\n");
        }
    }

    private void handleDownload() {
        // Code for handling
        String imageID = downloadField.getText();
        String response = clientHandler.sendCommand("DOWN " + imageID);

        String fileToGet = getFileName(imageID);

        FileOutputStream fos = null;
        File fileToDownload = new File("data/client/" + fileToGet);

        if (fileToDownload.exists()) {
            fileToDownload.delete();
        }

        try {
            clientHandler.downloadCommand(fileToDownload, response, fos);
            serverResponseArea.appendText("Command: " + "DOWN " + imageID + "\nSERVER> " + response + "\n");
            displayImage(fileToDownload.toURI().toString());
        } catch (Exception e) {
            DialogGUI.errorDialog("Download Error", "Download Error", "An error occurred while downloading the image.");
        }
    }

    private String getFileName(String imageID) {
        for (String image : imageList) {
            if (image.trim().startsWith(imageID)) {
                return image.trim().split(" ")[1];
            }
        }
        return null;
    }

    private void handleList() {
        fileListArea.clear();
        String response = clientHandler.sendCommand("LIST");

        if (!response.endsWith("error")) {
            imageList = response.split(":")[1].split("\\|");
            for (String image : imageList) {
                fileListArea.appendText(image + "\n");
            }
        }
    }
}
