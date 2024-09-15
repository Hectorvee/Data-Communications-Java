package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple server that listens for incoming client connections on a specified port.
 * It uses the BUKAHandler to manage individual client connections.
 */
public class Server {
    private static ServerSocket serverSocket;
    private static final int PORT = 2018;

    /**
     * The main method that starts the server and listens for incoming client connections.
     * It spawns a new thread to handle each client using the BUKAHandler class.
     *
     * @param argv Command line arguments (not used in this program).
     */
    public static void main(String[] argv) {
	//Setup server socket and pass on handling of request
        System.out.println("Starting server...");

        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new BUKAHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing server socket: " + e.getMessage());
            }
        }
    }
}
