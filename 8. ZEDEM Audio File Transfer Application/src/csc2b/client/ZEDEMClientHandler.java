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

    public String requestFile(String response, String fileName) {
        String requestStatus = "";

        // Send receive file
        try {
            // Read the file size
            int fileSize = Integer.parseInt(response);

            // Create file stream
            File fileToDownload = new File("data/client/" + fileName);
            FileOutputStream fos = new FileOutputStream(fileToDownload);

            // Read the file
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;

            while (totalBytes!=fileSize) {
                bytesRead = dis.read(buffer, 0, buffer.length);
                fos.write(buffer, 0, bytesRead);
                fos.flush();
                totalBytes += bytesRead;
            }

            fos.close();
            requestStatus = in.readLine();

        } catch (IOException e) {
            requestStatus = "Request Error: " + e.getMessage();
        }


        return requestStatus;
    }
}
