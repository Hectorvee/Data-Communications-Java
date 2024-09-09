package acsse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * The entry point of the server application.
 * Creates an instance of the server to start the server.
 * The server listens for incoming client connections and handles them in separate threads.
 */
public class WebServer {

    // Server port and maximum requests
    private static final int PORT = 4321;

    // Output and input streams
    private PrintWriter outputToClient;
    private BufferedReader inputFromClient;

    /**
     * Constructor to start the server
     */
    public WebServer() {
        startServer();
    }

    /**
     * Start the server and handle client requests
     */
    private void startServer() {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            // Display server start time
            System.out.println("Server started at " + new Date() + '\n');
            System.out.println("Ready for incoming requests...\n");

            // Accept client connections and handle requests
            while (true) {
                Socket clientConnection = serverSocket.accept();
                System.out.println("Client connected: " + clientConnection.getInetAddress().getHostName());
                new Thread(new ClientHandler(clientConnection)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
