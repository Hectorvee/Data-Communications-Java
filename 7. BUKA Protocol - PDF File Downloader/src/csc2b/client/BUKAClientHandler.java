package csc2b.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * BUKAClientHandler manages the connection to the server, sending commands, and downloading files.
 * It communicates with the server over a socket connection and provides methods to interact with the server.
 */
public class BUKAClientHandler {

    // Attributes
    private InetAddress host;
    private final int PORT = 2018;
    private Socket socket;
    private PrintWriter out;    // Output stream to send data to the server
    private BufferedReader in;  // Input stream to receive data from the server
    private DataInputStream dis;   // Input stream to receive files from the server

    /**
     * Constructor that initializes the client and connects it to the server.
     * It sets up the input and output streams for communication.
     */
    public BUKAClientHandler() {
        // Connect to server
        try {
            host = InetAddress.getLocalHost();
            socket = new Socket(host, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);    // Output stream to send data to the server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Input stream to receive data from the server
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));   // Input stream to receive files from the server

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Sends a command to the server and returns the server's response.
     *
     * @param command The command string to be sent to the server.
     * @return The server's response as a string.
     */
    public String sendCommand(String command) {
        String response = "";

        try {
            out.println(command);
            response = in.readLine();
            return response;
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Downloads a file from the server and saves it to the client's local directory.
     *
     * @param response The server's response containing the file size.
     * @param fileName The name of the file to be downloaded.
     * @return A status message indicating the result of the download operation.
     */
    public String downloadFile(String response, String fileName) {
        String downloadStatus = "";

        try {
            // Read the file size
            int fileSize = Integer.parseInt(response);

            // Create file streams
            File fileToDownload = new File("data/client/" + fileName);
            FileOutputStream fos = new FileOutputStream(fileToDownload);

            // Read the file from the server and write it to file
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;

            while (totalBytes != fileSize) {
                bytesRead = dis.read(buffer, 0, buffer.length);
                fos.write(buffer, 0, bytesRead);
                fos.flush();
                totalBytes += bytesRead;
            }
            fos.close();
            downloadStatus = in.readLine();

        } catch (IOException e) {
            downloadStatus = "Error: " + e.getMessage();
        }

        return downloadStatus;
    }

}
