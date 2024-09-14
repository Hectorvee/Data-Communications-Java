package csc2b.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BUKAClientHandler {

    // Attributes
    private InetAddress host;
    private final int PORT = 2024;
    private Socket socket;
    private PrintWriter out;    // Output stream to send data to the server
    private BufferedReader in;  // Input stream to receive data from the server
    private DataInputStream dis;   // Input stream to receive files from the server

    // Constructor
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

}
