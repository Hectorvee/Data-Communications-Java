package acsse.server;

import java.io.*;
import java.net.Socket;

/**
 * Handles client connections for the basic web server.
 * Processes HTTP requests and serves the appropriate files from the server.
 * Implements Runnable to handle each client connection in a separate thread.
 */
public class ClientHandler implements Runnable {

    // Client connection
    private final Socket clientConnection;
    private PrintWriter outputToClient;
    private BufferedReader inputFromClient;
    private DataOutputStream dataOutputStream;

    /**
     * Constructor to create a new client handler.
     * @param clientConnection The client connection.
     */
    public ClientHandler(Socket clientConnection) {
        this.clientConnection = clientConnection;
    }

    /**
     * Run the client handler.
     */
    @Override
    public void run() {
        try {
            setUpStreams();
            // output welcome message
            handleClientRequests();

        } catch (IOException e) {
            sendInternalServerError();
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    /**
     * Sets up the input and output streams for communication with the client.
     * @throws IOException If an I/O error occurs when opening the streams.
     */
    private void setUpStreams() throws IOException {
        outputToClient = new PrintWriter(clientConnection.getOutputStream(), true);
        inputFromClient = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
    }

    /**
     * Reads and processes client requests.
     * @throws IOException If an I/O error occurs while reading from the client.
     */
    private void handleClientRequests() throws IOException {
        // Read client request
        String clientRequest = inputFromClient.readLine();

        // Process client request
        if (clientRequest != null) {
            clientRequest = clientRequest.trim();
            handleRequest(clientRequest);
        }
    }

    /**
     * Handles the client request and sends the appropriate response.
     * @param clientRequest The client request.
     */
    private void handleRequest(String clientRequest) {
        String location = getLocation(clientRequest);
        File file = getFile(location);

        // Check if the file exists
        if (!validateFile(file)) return;

        try {
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientConnection.getOutputStream()));

            // Send response header
            String fileName = file.getName();

            if (fileName.contains("html")) {
                dataOutputStream.writeBytes(responseHeader("200 OK", "text/html", file.length()));
            } else if (fileName.contains("jpg")) {
                dataOutputStream.writeBytes(responseHeader("200 OK","image/jpeg", file.length()));
            } else {
                dataOutputStream.writeBytes(responseHeader("200 OK", "application/octet-stream", file.length()));
            }

            // Send file content
            FileInputStream fileInputStream = new FileInputStream(file);    // Read the file into the memory
            byte[] buffer = new byte[2048];   // Buffer to store the file content
            int bytesRead;  // Number of bytes read from the file

            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, bytesRead);  // Write the file content to the output stream
                dataOutputStream.flush();
            }

            // Close resources
            fileInputStream.close();
            dataOutputStream.close();


        } catch (IOException e) {
            // Send 500 Internal Server Error
            sendInternalServerError();
        }

    }

    /**
     * Gets the file from the specified location.
     * @param location The location of the file.
     * @return The file at the specified location.
     */
    private File getFile(String location) {
        if (location.equals("Joburg") || location.equals("Durban")) {
            return new File("data/" + location + ".html");
        } else if (location.equals("Cape")) {
            return new File("data/" + location + "WithImage.html");
        } else {
            return new File("data/" + location);
        }
    }

    /**
     * Validates the file and sends a 404 Not Found response if the file does not exist.
     * @param file The file to validate.
     * @return True if the file exists, false otherwise.
     */
    private boolean validateFile(File file) {

        if (!file.exists()) {
            sendFileNotFoundError();
            return false;
        }

        return true;
    }

    /**
     * Extracts the location from the client request.
     * @param clientRequest The client request.
     * @return The location extracted from the client request.
     */
    private String getLocation(String clientRequest) {
        return clientRequest.split("/", 2)[1].split(" ", 2)[0];
    }

    /**
     * Creates the response header.
     * @param statusCode The status code.
     * @param contentType The content type.
     * @param contentLength The content length.
     * @return The response header.
     */
    private String responseHeader(String statusCode, String contentType, long contentLength) {
        return "HTTP/1.1 " + statusCode + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + contentLength + "\r\n"
                + "\r\n";
    }

    /**
     * Sends a 500 Internal Server Error response to the client.
     */
    private void sendInternalServerError() {
        String errorHeader = responseHeader("500 Internal Server Error", "text/html", 0);
        try {
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientConnection.getOutputStream()));
            dataOutputStream.writeBytes(errorHeader);
            dataOutputStream.writeBytes(messageBody("500 Internal Server Error"));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error sending 500 Internal Server Error.");
        }
    }

    /**
     * Sends a 404 Not Found response to the client.
     */
    private void sendFileNotFoundError() {
        String errorHeader = responseHeader("404 Not Found", "text/html", 0);
        try {
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientConnection.getOutputStream()));
            dataOutputStream.writeBytes(errorHeader);
            dataOutputStream.writeBytes(messageBody("404 Not Found"));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error sending 404 Not Found.");
        }
    }

    /**
     * Creates the message body.
     * @param message The message to display in the body.
     * @return The message body.
     */
    private String messageBody(String message) {
        return "<html><body><h1>" + message + "</h1></body></html>";
    }

    /**
     * Closes the input and output streams and the client connection.
     */
    private void closeResources() {
        try {
            if (outputToClient != null) {
                outputToClient.close();
            }
            if (inputFromClient != null) {
                inputFromClient.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (clientConnection != null) {
                clientConnection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
