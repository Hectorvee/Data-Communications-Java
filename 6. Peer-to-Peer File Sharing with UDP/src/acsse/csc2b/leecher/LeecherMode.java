package acsse.csc2b.leecher;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A class representing the Leecher mode of the application.
 * The LeecherMode class extends GridPane and provides a GUI for file leeching,
 * allowing users to connect to a Seeder, list files, and download files.
 */
public class LeecherMode extends GridPane {

    // GUI elements
    private DatagramSocket socket;
    private TextField ipField;
    private TextField portField;
    private ListView<String> fileListView;
    private TextArea logArea;
    private Button btnConnect;
    private Button btnDownload;
    private Button btnList;
    private Label instructionLabel;

    /**
     * Constructor to initialize and set up the LeecherMode GUI.
     */
    public LeecherMode() {
        // Set up GUI elements
        ipField = new TextField();
        portField = new TextField();
        fileListView = new ListView<>();
        logArea = new TextArea();
        btnConnect = new Button("Connect to Seeder");
        btnDownload = new Button("Download File");
        btnList = new Button("List Files");
        instructionLabel = new Label("Select a file to download");

        // Set up GUI properties
        ipField.setPromptText("Enter Seeder IP (localhost)");
        portField.setPromptText("Enter Seeder Port (1338)");
        ipField.setText("localhost");
        portField.setText("1338");

        // Set up event handlers
        btnConnect.setOnAction(e -> connectToSeeder());
        btnDownload.setOnAction(e -> downloadSelectedFile());
        btnList.setOnAction(e -> listFilesFromSeeder());

        // Add GUI elements to the GridPane
        this.add(ipField, 0, 0);
        this.add(portField, 0, 1);
        this.add(btnConnect, 0, 2);
        this.add(btnList, 0, 3);
        this.add(fileListView, 0, 4);
        this.add(instructionLabel, 0, 5);
        this.add(btnDownload, 0, 6);
        this.add(logArea, 0, 7);

        // Set up the GridPane
        setupGridPane();
    }

    /**
     * Connect to the Seeder and receive the list of files available for download.
     */
    private void connectToSeeder() {
        try {
            String host = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);

            // Send LIST command to Seeder
            byte[] sendData = "LIST".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

            // Receive file list from Seeder
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String fileList = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] files = fileList.split("\n");

            // Display files in the ListView
            fileListView.getItems().clear();
            for (String file : files) {
                fileListView.getItems().add(file);
            }

            logArea.appendText("Connected to Seeder and received file list\n");
        } catch (Exception e) {
            logArea.appendText("Error connecting to Seeder: " + e.getMessage() + "\n");
        }
    }

    /**
     * Download the selected file from the Seeder.
     */
    private void downloadSelectedFile() {
        try {
            int selectedIndex = fileListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedFile = fileListView.getItems().get(selectedIndex);
                String host = ipField.getText();
                int port = Integer.parseInt(portField.getText());
                InetAddress address = InetAddress.getByName(host);

                // Send FILE command to Seeder
                String command = "FILE " + selectedFile;
                byte[] sendData = command.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                socket.send(sendPacket);

                // Receive file data from Seeder
                byte[] receiveData = new byte[2048];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Size" + receivePacket.getLength());
                ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();

                while (true) {
                    socket.receive(receivePacket);
                    if (receivePacket.getLength() == 0) break; // End of file transmission
                    fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
                }

                // Save the received file automatically in data/leecher folder
                File directory = new File("data/leecher");
                File file = new File(directory, selectedFile);
                FileOutputStream fos = new FileOutputStream(file);
                fileOutputStream.writeTo(fos);

                logArea.appendText("Downloaded file: " + selectedFile + "\n");
            }
        } catch (Exception e) {
            logArea.appendText("Error downloading file: " + e.getMessage() + "\n");
        }
    }

    /**
     * Set up the GridPane layout for the LeecherMode GUI.
     */
    private void setupGridPane() {
        // Set up GUI elements
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setHgap(10);
        this.setVgap(10);
    }

    /**
     * List files available for download from the Seeder.
     */
    private void listFilesFromSeeder() {
        try {
            String host = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            InetAddress address = InetAddress.getByName(host);

            // Send LIST command to Seeder
            byte[] sendData = "LIST".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

            // Receive file list from Seeder
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String fileList = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] files = fileList.split("\n");

            // Display files in the ListView
            fileListView.getItems().clear();
            for (String file : files) {
                fileListView.getItems().add(file);
            }

            logArea.appendText("Received file list from Seeder\n");
        } catch (Exception e) {
            logArea.appendText("Error listing files from Seeder: " + e.getMessage() + "\n");
        }
    }
}
