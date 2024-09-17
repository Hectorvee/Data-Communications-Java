package csc2b.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ZEDEMClientHandler {

    private InetAddress host;
    private Socket socket;
    private final int PORT = 2024;
    private PrintWriter out;
    private BufferedReader in;
    private DataInputStream dis;

    public ZEDEMClientHandler() {

        try {
            host = InetAddress.getLocalHost();
            socket = new Socket(host, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());;
        }
    }

    public String sendCommand(String command) {
        String response = "";

        try {
            out.println(command);
            response = in.readLine();
        } catch (IOException e) {
            response = "Error: " + e.getMessage();;
        }

        return response;
    }
}
