package csc2b.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * The BUKAClientPane class defines a JavaFX pane with various GUI components for interacting
 * with a BUKA server. This includes logging in, listing available files, retrieving a file,
 * and logging out.
 * The client communicates with the server via the BUKAClientHandler class. The layout is organized
 * using a GridPane for positioning components.
 */
public class BUKAClientPane extends GridPane {
    // GUI Elements
    private TextField nameField;
    private TextField passField;
    private TextField idField;
    private TextArea listArea;
    private TextArea serverArea;
    private Button btnLogin;
    private Button btnList;
    private Button btnRetrieve;
    private Button btnLogout;

    private BUKAClientHandler clientHandler;
    private boolean isLogin = false;
    private String[] fileList;

    /**
     * Constructs a new BUKAClientPane. Initializes the client handler, GUI elements,
     * and sets up the layout and button actions.
     */
    public BUKAClientPane() {
        clientHandler = new BUKAClientHandler();
        nameField = new TextField();
        passField = new TextField();
        idField = new TextField();
        listArea = new TextArea();
        serverArea = new TextArea();
        btnLogin = new Button("LOGIN");
        btnList = new Button("LIST");
        btnRetrieve = new Button("RETRIEVE");
        btnLogout = new Button("LOGOUT");
        init();
    }

    /**
     * Initializes the layout, sets up the grid, button actions, and prompt text for input fields.
     */
    private void init() {

        // Set up Prompt text
        nameField.setPromptText("Name");
        passField.setPromptText("Password");
        idField.setPromptText("PDF ID");
        listArea.setPromptText("LIST PDF Files");
        serverArea.setPromptText("Server Area Response");

        // Set up grid layout
        setupGridLayout();

        // Set up button actions
        btnLogin.setOnAction(e -> login());
        btnList.setOnAction(e -> requestFileList());
        btnRetrieve.setOnAction(e -> requestFile());
        btnLogout.setOnAction(e -> logOut());

        // Add elements on the grid
        add(nameField, 0, 0);
        add(passField, 1, 0);
        add(btnLogin, 2, 0);
        add(btnList, 0, 1);
        add(new ScrollPane(listArea), 0, 2);
        add(idField, 0, 3);
        add(btnRetrieve, 1, 3);
        add(new ScrollPane(serverArea), 0, 4);
        add(btnLogout, 0, 5);
    }

    /**
     * Configures the grid layout by setting alignment, padding, and spacing between elements.
     */
    private void setupGridLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(10);
        setHgap(10);
    }

    /**
     * Logs in the user by sending an authentication command to the server using the
     * provided username and password. If successful, the file list is requested.
     */
    private void login() {

        if (!isLogin) {
            String command = nameField.getText().trim() + " " + passField.getText().trim();
            String response = clientHandler.sendCommand("AUTH " + command);

            String statusCode = response.split(" ")[0];
            if (statusCode.equals("200")) {
                isLogin = true;
                serverArea.appendText(response + "\n");
                requestFileList();
            } else {
                serverArea.appendText(response + "\n");
            }

        } else {
            serverArea.appendText("Already logged in\n");
        }
    }

    /**
     * Requests a list of files from the server. The list is displayed in the listArea.
     * If the user is not logged in, a message is displayed prompting them to log in.
     */
    private void requestFileList() {
        if (!isLogin) {
            serverArea.appendText("Please login first\n");
            return;
        }

        String response = clientHandler.sendCommand("LIST");
        // response example: 200 1 file1|2 file2|3 file3
        String responseCode = response.split(" ", 2)[0];
        String responseMsg = response.split(" ", 2)[1];
        if (responseCode.equals("200")) {
            fileList = responseMsg.split("\\|");
            listArea.clear();
            for (String fileName: fileList) {
                listArea.appendText(fileName + "\n");
            }
            serverArea.appendText(responseCode + " List Successful\n");
        } else {
            serverArea.appendText(response + "\n");
        }
    }

    /**
     * Requests a file from the server using the provided file ID. The file is downloaded
     * and saved to the client's local directory. If the user is not logged in, a message
     * is displayed prompting them to log in.
     */
    private void requestFile() {
        if (!isLogin) {
            serverArea.appendText("Please login first\n");
            return;
        }

        String imageID = idField.getText().trim();

        // Check if file ID exist from the file list
        if (!imageIdExist(imageID)) {
            serverArea.appendText("File with ID " + imageID + " does not exist");
            return;
        }

        // Send command
        String response = clientHandler.sendCommand("PDFRET " + imageID);

        // Get file name
        String fileName = getFilename(imageID);

        // Download the file
        String downloadStatus = clientHandler.downloadFile(response, fileName);

        serverArea.appendText(downloadStatus + "\n");
    }

    /**
     * Logs out the user by sending a logout command to the server. The user is then
     * prompted to log in again to access the server's services.
     */
    private void logOut() {
        String response = clientHandler.sendCommand("LOGOUT");
        serverArea.appendText(response + "\n");
    }

    /**
     * Checks if the provided image ID exists in the file list.
     *
     * @param imageID The image ID to check.
     * @return true if the image ID exists; false otherwise.
     */
    private boolean imageIdExist(String imageID) {
        for (String file: fileList) {
            if (imageID.equals(file.split(" ")[0])) return true;
        }

        return false;
    }

    /**
     * Gets the filename associated with the provided image ID from the file list.
     *
     * @param imageID The image ID to get the filename for.
     * @return The filename associated with the image ID.
     */
    public String getFilename(String imageID) {

        for (String file: fileList) {
            if (imageID.equals(file.split(" ")[0])) return file.split(" ", 2)[1];
        }

        return " ";
    }

}
