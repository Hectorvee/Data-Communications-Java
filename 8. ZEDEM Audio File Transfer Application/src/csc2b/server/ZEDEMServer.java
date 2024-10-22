package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ZEDEMServer {

    private static ServerSocket serverSocket;
    private static final int PORT = 2024;

    public static void main(String[] argv) {
        //Setup server socket and pass on handling of request
        System.out.println("Starting server...");

        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ZEDEMHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
