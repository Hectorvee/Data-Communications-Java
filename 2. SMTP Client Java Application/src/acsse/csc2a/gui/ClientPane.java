package acsse.csc2a.gui;

import acsse.csc2a.handler.ClientHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * ClientPane is the main UI class for managing SMTP email connections and sending emails.
 * It extends BorderPane to provide a flexible layout for the connection and email interface.
 */
public class ClientPane extends BorderPane {

    // Client handler
    ClientHandler clientHandler = null;

    // UI components for SMTP connection
    private GridPane connectionGrid;
    private TextField hostNameField;
    private TextField portNumberField;
    private Label hostNameLabel;
    private Label porNumbertLabel;
    private Button btnConnect;
    private Text titleText;
    private HBox titleContainer;

    // UI components for email message
    private GridPane emailGrid;
    private Label fromLabel;
    private Label toLabel;
    private Label subjectLabel;
    private Label messageLabel;
    private Label attachedLabel;
    private TextField fromField;
    private TextField toField;
    private TextField subjectField;
    private TextArea messageField;
    private Button btnSend;
    private Button btnAttach;
    private File attachedFile;

    /**
     * Constructor to initialize the ClientPane and set up the GUI.
     */
    public ClientPane() {
        initGUI();
    }

    /**
     * Initializes the GUI components and layout for the SMTP connection interface.
     */
    private void initGUI() {
        connectionGrid = new GridPane();
        hostNameField = new TextField();
        hostNameLabel = new Label("Hostname: ");
        portNumberField = new TextField();
        porNumbertLabel = new Label("Port Number: ");
        btnConnect = new Button("Connect");

        // Set action for the Connect button
        btnConnect.setOnAction(e -> handleConnect());

        // Add components to the connectionGrid
        connectionGrid.add(hostNameLabel, 0, 0);
        connectionGrid.add(hostNameField, 1, 0);
        connectionGrid.add(porNumbertLabel, 0, 1);
        connectionGrid.add(portNumberField, 1, 1);
        connectionGrid.add(btnConnect, 1, 2);

        // Set up the title and layout for the connection interface
        setUpTitleContainer("SMTP Connection");
        setUpGrid(connectionGrid);

        // Add components to the BorderPane
        this.setTop(titleContainer);
        this.setCenter(connectionGrid);
    }

    /**
     * Sets up the email message interface after a successful connection.
     */
    private void clientGUI() {
        this.setCenter(null);
        setUpTitleContainer("Email Message");

        emailGrid = new GridPane();
        fromLabel = new Label("From: ");
        toLabel = new Label("To: ");
        subjectLabel = new Label("Subject: ");
        messageLabel = new Label("Message: ");
        attachedLabel = new Label("No file chosen");
        fromField = new TextField();
        toField = new TextField();
        subjectField = new TextField();
        messageField = new TextArea();
        btnSend = new Button("Send");
        btnAttach = new Button("Choose File");

        // Set action for the Send button
        btnSend.setOnAction(e -> handleSend());

        // Set action for the Attachment button
        btnAttach.setOnAction(e -> handleAttach());

        // Add components to the emailGrid
        emailGrid.add(new Label("Host: "), 0, 0);
        emailGrid.add(new Label(hostNameField.getText().trim()), 1, 0);
        emailGrid.add(new Label("Port: "), 0, 1);
        emailGrid.add(new Label(portNumberField.getText().trim()), 1, 1);
        emailGrid.add(fromLabel, 0, 2);
        emailGrid.add(fromField, 1, 2);
        emailGrid.add(toLabel, 0, 3);
        emailGrid.add(toField, 1, 3);
        emailGrid.add(subjectLabel, 0, 4);
        emailGrid.add(subjectField, 1, 4, 2, 1);
        emailGrid.add(messageLabel, 0, 5);
        emailGrid.add(messageField, 1, 5, 2, 1);
        emailGrid.add(btnAttach, 1, 6);
        emailGrid.add(attachedLabel, 2, 6);
        emailGrid.add(btnSend, 1, 7);

        // Set up the layout for the emailGrid
        setUpGrid(emailGrid);

        // Update the BorderPane with the email interface
        this.setTop(titleContainer);
        this.setCenter(emailGrid);

    }

    /**
     * Handles the connection to the SMTP server based on user input.
     */
    private void handleConnect() {
        String host = hostNameField.getText().trim();
        String portText = portNumberField.getText().trim();

        // Connect and switch to email interface if successful
        if (validateHostAndPort(host, portText)) {
            int port = Integer.parseInt(portText);
            clientHandler = new ClientHandler(host, port);

            if (clientHandler.connect()) {
                clientGUI();
            }
        }
    }

    /**
     * Handles the sending of the email based on user input.
     */
    private void handleSend() {
        String from = fromField.getText().trim();
        String to = toField.getText().trim();
        String subject = subjectField.getText().trim();
        String message = messageField.getText().trim();

        if (verifyEmailFields(from, to, subject, message)) {
            if (clientHandler.sendEmail(from, to, subject, message, attachedFile)) {
                // Display success message
                DialogGUI.successDialog("Email Sent", "Email Sent Successfully", "The email was sent successfully.");
            } else {
                // Display error message
                DialogGUI.errorDialog("Email Error", "Email Not Sent", "The email was not sent. Please try again.");
            };
        }
    }

    /**
     * Handles the attachment of a file to the email.
     */
    private void handleAttach() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attach File");
        attachedFile = fileChooser.showOpenDialog(null);
        if (attachedFile != null) {
            attachedLabel.setText(attachedFile.getName());
            DialogGUI.successDialog("File Attached", "File Attached Successfully", "The file was attached successfully.");
        } else {
            DialogGUI.errorDialog("File Error", "File Not Attached", "The file was not attached. Please try again.");
        }
    }

    /**
     * Sets up the title container with a given title.
     * @param title The title text to display.
     */
    private void setUpTitleContainer(String title) {
        titleText = new Text(title);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFont(Font.font("Verdana", 20));
        titleText.setStyle("-fx-font-weight: bold; -fx-fill: #5c5f5d");

        // Wrap titleText in a container and center it
        titleContainer = new HBox(titleText);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(10, 10, 10, 10)); // Top, right, bottom, left padding
    }

    /**
     * Sets up the grid layout for the given GridPane.
     * @param grid The GridPane to set up.
     */
    private void setUpGrid(GridPane grid) {
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20)); // Top, right, bottom, left padding
        grid.setVgap(10);  // Vertical spacing between rows
        grid.setHgap(10);  // Horizontal spacing between columns
    }

    /**
     * Validates the hostname and port number input.
     * @param text The hostname input.
     * @param portText The port number input.
     * @return True if the input is valid, false otherwise.
     */
    private boolean validateHostAndPort(String text, String portText) {
        int portNum;
        try {
            portNum = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            DialogGUI.errorDialog("Invalid Port Number", "Invalid Port Number", "Port number must be a number");
            return false;
        }

        if (portNum < 0 || portNum > 65535) {
            DialogGUI.errorDialog("Invalid Port Number", "Invalid Port Number", "Port number must be between 0 and 65535");
            return false;
        }

        if (text.isEmpty()) {
            DialogGUI.errorDialog("Invalid Hostname", "Invalid Hostname", "Hostname cannot be empty");
            return false;
        }

        return true;
    }

    /**
     * Verifies the email fields before sending the email.
     * @param from The sender's email address.
     * @param to The recipient's email address.
     * @param subject The email subject.
     * @param message The email message.
     * @return True if the fields are valid, false otherwise.
     */
    private boolean verifyEmailFields(String from, String to, String subject, String message) {
        if (from.isEmpty() || to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            DialogGUI.errorDialog("Invalid Email Fields", "Invalid Email Fields", "All fields must be filled in");
            return false;
        }
        return true;
    }
}
