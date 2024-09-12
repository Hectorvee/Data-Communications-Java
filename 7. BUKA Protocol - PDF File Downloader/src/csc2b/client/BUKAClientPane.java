package csc2b.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class BUKAClientPane extends GridPane //You may change the JavaFX pane layout
{
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

    public BUKAClientPane() {
	//Create client connection
	//Create buttons for each command
	//Use buttons to send commands
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

    private void setupGridLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(10);
        setHgap(10);
    }

    private void login() {
        String command = nameField.getText().trim() + " " + passField.getText().trim();
        String response = clientHandler.sendCommand(command);

        String statusCode = response.split(" ")[0];
        if (statusCode.equals("200")) isLogin = true;

        serverArea.appendText(response + "\n");
    }

    private void requestFileList() {

    }

    private void requestFile() {

    }

    private void logOut() {

    }

}
