package acsse.csc2b.seeder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Seeder mode of the application.
 * The SeederMode class extends GridPane and provides a GUI for file seeding,
 * allowing users to add files, start the seeder, and handle file requests.
 */
public class SeederMode extends GridPane {

    // GUI elements
    private TextArea logArea;
    private Button btnAddFile;
    private Button btnStartSeeder;
    private ListView<String> fileListView;
    private List<File> sharedFiles;
    private DatagramSocket socket;

    /**
     * Constructor to initialize and set up the SeederMode GUI.
     */
    public SeederMode() {
        // Set up GUI elements
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setHgap(10);
        this.setVgap(10);

        logArea = new TextArea();
        btnAddFile = new Button("Add File");
        btnStartSeeder = new Button("Start Seeder");
        fileListView = new ListView<>();
        sharedFiles = new ArrayList<>();

        // Set up event handlers
        btnAddFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/seeder"));
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                sharedFiles.add(file);
                fileListView.getItems().add(file.getName());
                logArea.appendText("Added file: " + file.getName() + "\n");
            }
        });

        btnStartSeeder.setOnAction(e -> startSeeder());

        this.add(fileListView, 0, 0);
        this.add(btnAddFile, 0, 1);
        this.add(btnStartSeeder, 0, 2);
        this.add(logArea, 0, 3);
    }

    /**
     * Starts the seeder by creating a DatagramSocket and listening for incoming requests.
     */
    private void startSeeder() {
        try {
            socket = new DatagramSocket(1338);
            logArea.appendText("Seeder started, listening for connections...\n");
            new Thread(() -> listenForRequests()).start();
        } catch (Exception e) {
            logArea.appendText("Error starting Seeder: " + e.getMessage() + "\n");
        }
    }

    /**
     * Listens for incoming requests and handles LIST and FILE commands.
     */
    private void listenForRequests() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            while (true) {
                // Listen for LIST or FILE commands (refer to earlier code structure)
                socket.receive(packet);
                String request = new String(packet.getData(), 0, packet.getLength());

                // Handle LIST or FILE commands
                if (request.equals("LIST")) {
                    sendFileList(packet);
                } else if (request.startsWith("FILE")) {
                    sendFile(request, packet);
                }
            }
        } catch (Exception e) {
            logArea.appendText("Error handling requests: " + e.getMessage() + "\n");
        }
    }

    /**
     * Sends the list of shared files to the requesting client.
     * @param packet The DatagramPacket containing the request.
     */
    private void sendFileList(DatagramPacket packet) {
        StringBuilder fileList = new StringBuilder();
        for (File file : sharedFiles) {
            fileList.append(file.getName()).append("\n");
        }
        byte[] data = fileList.toString().getBytes();
        DatagramPacket fileListPacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
        try {
            socket.send(fileListPacket);
        } catch (Exception e) {
            logArea.appendText("Error sending file list: " + e.getMessage() + "\n");
        }
    }

    /**
     * Sends the requested file to the requesting client.
     * @param request The request string containing the file name.
     * @param packet The DatagramPacket containing the request.
     */
    private void sendFile(String request, DatagramPacket packet) throws IOException {
        String[] parts = request.split(" ");
        String fileName = parts[1];
        File file = new File("data/seeder/" + fileName);

        if (!file.exists()) {
            String errorMessage = "ERROR: File not found";
            byte[] errorResponse = errorMessage.getBytes();
            DatagramPacket errorPacket = new DatagramPacket(errorResponse, errorResponse.length, packet.getAddress(), packet.getPort());
            socket.send(errorPacket);
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[2048];
        int bytesRead;


        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            DatagramPacket filePacket = new DatagramPacket(buffer, bytesRead, packet.getAddress(), packet.getPort());
            socket.send(filePacket);
        }

        // Send an empty packet to indicate the end of the file transmission
        DatagramPacket endPacket = new DatagramPacket(new byte[0], 0, packet.getAddress(), packet.getPort());
        socket.send(endPacket);
    }
}
