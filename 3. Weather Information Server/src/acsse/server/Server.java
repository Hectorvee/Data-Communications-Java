package acsse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The entry point of the server application.
 * Creates an instance of the server to start the server.
 */
public class Server {

    // Server port and maximum requests
    private static final int PORT = 8888;
    private static final int MAX_REQUESTS = 4;

    // Output and input streams
    private PrintWriter outputToClient;
    private BufferedReader inputFromClient;
    private int remainingRequests = MAX_REQUESTS;
    private int requestNo = 1;
    private boolean isSessionActive = false;

    /**
     * Constructor to start the server
     */
    public Server() {
        startServer();
    }

    /**
     * Start the server and handle client requests
     */
    private void startServer() {

        ServerSocket serverSocket = null;
        Socket clientConnection = null;

        try {
            serverSocket = new ServerSocket(PORT);
            // Display server start time
            System.out.println("Server started at " + new java.util.Date() + '\n');
            System.out.println("Ready for incoming requests...\n");

            // Accept client connection
            clientConnection = serverSocket.accept();
            setUpStreams(clientConnection);
            outputToClient.println("0" + (requestNo++) + " HELLO - you may make " + MAX_REQUESTS + " requests and I will provide weather information.");
            handleClientRequests();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputToClient != null) {
                    outputToClient.close();
                }
                if (inputFromClient != null) {
                    inputFromClient.close();
                }
                if (clientConnection != null) {
                    clientConnection.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set up input and output streams
     * @param clientConnection Socket connection to client
     * @throws IOException if an I/O error occurs when creating the input and output streams
     */
    private void setUpStreams(Socket clientConnection) throws IOException {
        outputToClient = new PrintWriter(clientConnection.getOutputStream(), true);
        inputFromClient = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
    }

    /**
     * Handle client requests
     * @throws IOException if an I/O error occurs when reading from the input stream
     */
    private void handleClientRequests() throws IOException {
        while (true) {

            // Read client request
            String clientRequest = inputFromClient.readLine().trim();

            // Validate client request
            if (!validateRequest(clientRequest)) {
                continue;
            }

            // Start session
            if (!isSessionActive && clientRequest.equals("START")) {
                isSessionActive = true;
                outputToClient.println("REQUEST or DONE");
                continue;
            }

            // Check if session is active
            if (!isSessionActive) {
                outputToClient.println("Invalid request. Please type 'START' to begin a session.");
                continue;
            }

            // End session
            if (clientRequest.equals("DONE")) {
                outputToClient.println("0" + (requestNo++) + " GOOD BYE - " + (MAX_REQUESTS - remainingRequests) + " queries answered");
                break;
            }

            // Handle client request
            if (clientRequest.startsWith("REQUEST")) {
                handleRequest(clientRequest);
            } else {
                outputToClient.println("Unknown command. Type 'REQUEST [location]' or 'DONE' to end.");
            }

            // Check if remaining requests is less than or equal to 0
            if (remainingRequests <= 0) {
                outputToClient.println("GOOD BYE - " + MAX_REQUESTS + " queries answered");
                break;
            }
        }

    }

    /**
     * Handle client request and provide weather information
     * @param clientRequest client request
     */
    private void handleRequest(String clientRequest) {
        String location = clientRequest.substring(clientRequest.indexOf(" ")+ 1).trim();
        String weatherInfo = getWeatherInfo(location);
        outputToClient.println(weatherInfo);
        remainingRequests--;
    }

    /**
     * Get weather information
     * @param location location
     * @return weather information
     */
    private String getWeatherInfo(String location) {
        return switch (location.toLowerCase()) {
            case "johannesburg" -> "0" + (requestNo++) + " Clear Skies in Joburg";
            case "durban" -> "0" + (requestNo++) + " Sunny and Warm in Durban";
            case "cape town" -> "0" + (requestNo++) + " Cool and Cloudy in Cape Town";
            default -> "0" + (requestNo++) + location + " data outdated";
        };
    }

    /**
     * Validate client request command
     * @param clientRequest client request
     * @return true if client request is valid, false otherwise
     */
    private boolean validateRequest(String clientRequest) {
        if (clientRequest.isEmpty()) {
            outputToClient.println("Command cannot be empty");
            return false;
        }

        // Split client request into parts
        String[] parts = clientRequest.split(" ", 2);
        String command = parts[0].toUpperCase();

        if (!command.equals("START") && !command.equals("REQUEST") && !command.equals("DONE")) {
            outputToClient.println("Command must start with 'START' or 'REQUEST' or 'DONE'");
            return false;
        }

        if (command.equals("REQUEST") && parts.length < 2) {
            outputToClient.println("REQUEST command must have at least one argument");
            return false;
        }

        if ((command.equals("START") || command.equals("DONE")) && parts.length > 1) {
            outputToClient.println("START command must not have any arguments");
            return false;
        }

        return true;
    }
}
