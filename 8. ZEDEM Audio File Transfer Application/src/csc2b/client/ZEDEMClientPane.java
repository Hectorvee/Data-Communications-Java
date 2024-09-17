package csc2b.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ZEDEMClientPane extends GridPane {

    // GUI elements
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField idField;
    private TextArea listArea;
    private TextArea responseArea;
    private Button btnLogin;
    private Button btnList;
    private Button btnRequest;
    private Button btnLogout;

    private ZEDEMClientHandler clientHandler;
    private boolean isLogin = false;
    private String[] mp3List;


    public ZEDEMClientPane() {
        //Create client connection
        //Create buttons for each command
        //Use buttons to send commands

        clientHandler = new ZEDEMClientHandler();
        usernameField = new TextField();
        passwordField = new PasswordField();
        idField = new TextField();
        listArea = new TextArea();
        responseArea = new TextArea();
        btnLogin = new Button("Login");
        btnList = new Button("LIST");
        btnRequest = new Button("REQUEST");
        btnLogout = new Button("LOGOUT");
        initGUI();
    }

    private void initGUI() {
        // Label text fields
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        idField.setPromptText("mp3 ID");
        listArea.setPromptText("File List");
        responseArea.setPromptText("Server Response");

        // Setup gridlayout
        setupGridLayout();

        // Set actions for the buttons
        btnLogin.setOnAction(e -> logIn());
        btnList.setOnAction(e -> listFile());
        btnRequest.setOnAction(e -> requestFile());
        btnLogout.setOnAction(e -> logOut());

        // Put elements on the GUI
        add(usernameField, 0, 0);
        add(passwordField, 1, 0);
        add(btnLogin, 2, 0);
        add(btnList, 0, 1);
        add(new ScrollPane(listArea), 0, 2);
        add(idField, 0, 3);
        add(btnRequest, 1, 3);
        add(new ScrollPane(responseArea), 0, 4);
        add(btnLogout, 0, 5);
    }

    private void setupGridLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(10);
        setHgap(10);
    }

    private void logIn() {

        // Check if already logged in
        if (isLogin) {
            responseArea.appendText("Already logged in. Start making request to the server.\n");
            return;
        }

        // Check if fields are not empty
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            responseArea.appendText("Enter BOTH username and password\n");
            return;
        }

        // Send authentication to the server
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String response = clientHandler.sendCommand("BONJOUR " + username + " " + password);

        // Check response
        String responseCode = response.split(" ", 2)[0];
        if (responseCode.equals("NEE")) {
            responseArea.appendText(response + "\n");
            return;
        }

        isLogin = true;
        listFile();
    }

    private void listFile() {
        // Check if is logged in
        if (!isLogin) {
            responseArea.appendText("Login first.\n");
            return;
        }

        String response = clientHandler.sendCommand("PLAYLIST");
        if (response.startsWith("JA")) {
            listArea.clear();
            mp3List = response.split(" ", 2)[1].split("\\|");
            for (String file: mp3List) {
                listArea.appendText(file + "\n");
            }

            responseArea.appendText("Ja List Successful");
        } else {
            responseArea.appendText(response + "\n");
        }

    }

    private void requestFile() {

    }

    private void logOut() {

    }
}
