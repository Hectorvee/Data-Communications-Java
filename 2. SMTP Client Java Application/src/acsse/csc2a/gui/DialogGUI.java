package acsse.csc2a.gui;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying dialog boxes to the user.
 * Provides methods for error and success notifications.
 */
public class DialogGUI {

    /**
     * Displays an error dialog with the specified title, header text, and content text.
     * @param title The title of the error dialog.
     * @param headerText The header text of the error dialog.
     * @param contentText The content text of the error dialog.
     */
    public static void errorDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Displays a success dialog with the specified title, header text, and content text.
     * @param title The title of the success dialog.
     * @param headerText The header text of the success dialog.
     * @param contentText The content text of the success dialog.
     */
    public static void successDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
