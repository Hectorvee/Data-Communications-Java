package acsse.csc2a.handler;

import acsse.csc2a.gui.DialogGUI;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Handles the SMTP client-server communication.
 */
public class ClientHandler {
    private Socket socket;
    private String host;
    private int port;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructor to initialize the client handler with the specified host and port.
     * @param host The host address of the SMTP server.
     * @param port The port number to connect to the SMTP server.
     */
    public ClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = null;
    }

    /**
     * Method to connect to the server.
     * @return true if the connection was successful, false otherwise.
     */
    public boolean connect() {

        boolean status = false;
        try {
            // Attempt to establish a connection to the server
            socket = new Socket(host, port);
            DialogGUI.successDialog("Connection Successful", "Connection Established", "Successfully connected to the port.");
            status =  true;
        } catch (UnknownHostException e) {
            DialogGUI.errorDialog("Connection Error", "Unable to Resolve Host", "The specified host could not be resolved.");
        } catch (IOException e) {
            DialogGUI.errorDialog("Connection Error", "I/O Error", "An I/O error occurred while connecting to the port.");
        } catch (Exception e) {
            DialogGUI.errorDialog("Unknown Error", "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }

        return status;
    }

    /**
     * Method to send an email message to the server.
     * @param from The sender's email address.
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param message The message body of the email.
     * @param attachedFile The file to attach to the email.
     * @return true if the email was sent successfully, false otherwise.
     */
    public boolean sendEmail(String from, String to, String subject, String message, File attachedFile) {

        try {

            // Initialize input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Check server's initial response
            if (!verifyResponse(in.readLine(), "220")) return false;

            // Send HELO command to the server
            out.println("HELO " + host);
            if (!verifyResponse(in.readLine(), "250")) return false;

            // Send MAIL FROM command
            out.println("MAIL FROM:<" + from + ">");
            if (!verifyResponse(in.readLine(), "250")) return false;

            // Send RCPT TO command
            out.println("RCPT TO:<" + to + ">");
            if (!verifyResponse(in.readLine(), "250")) return false;

            // Send DATA command
            out.println("DATA");
            if (!verifyResponse(in.readLine(), "354")) return false;

            // Send the email message
            out.println("Subject: " + subject);
            out.println("From: " + from);
            out.println("To: " + to);
            out.println();
            out.println(message);

            // Send the attachment if it exists
            if (attachedFile != null) {
                out.println();
                out.println("Attached File: " + attachedFile.getName());
                out.println();
                out.println("File Contents:");
                out.println();
                BufferedReader fileReader = new BufferedReader(new FileReader(attachedFile));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
                }
                fileReader.close();
            }

            out.println(".");
            if (!verifyResponse(in.readLine(), "250")) return false;

            // Quit the connection
            out.println("QUIT");
            if (!verifyResponse(in.readLine(), "221")) return false;

        } catch (IOException e) {
            DialogGUI.errorDialog("Connection Error", "I/O Error", e.getMessage());
            return false;
        } finally {
            try {
                // Ensure resources are closed
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }

                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (Exception e) {
                DialogGUI.errorDialog("Connection Error", "Socket Error", "An error occurred while closing the socket.");
            }
        }

        return true;
    }

    /**
     * Method to verify the server's response code.
     * @param response The server's response message.
     * @param expectedCode The expected response code.
     * @return true if the response code matches the expected code, false otherwise.
     */
    private boolean verifyResponse(String response, String expectedCode) {
        return response != null && response.startsWith(expectedCode);
    }
}