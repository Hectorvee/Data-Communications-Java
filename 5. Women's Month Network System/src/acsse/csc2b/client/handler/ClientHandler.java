package acsse.csc2b.client.handler;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientHandler {
    private InetAddress host;
    private final int PORT = 5432;
    private Socket link;
    private BufferedReader in;
    private PrintWriter out;
    private DataInputStream dis;
    private DataOutputStream dos;
    public ClientHandler() {

        // Get the local host
        try {
            host = InetAddress.getLocalHost();
            link = new Socket(host, PORT);
            in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            out = new PrintWriter(link.getOutputStream(), true);
            dis = new DataInputStream(new BufferedInputStream(link.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(link.getOutputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void downloadCommand(File fileToDownload, String response, FileOutputStream fos) throws IOException {
        int fileSize = Integer.parseInt(response);

        fos = new FileOutputStream(fileToDownload);
        byte[] buffer = new byte[1024];
        int n;
        int totalBytes = 0;

        // Read the image data from the client and write it to the file
        while (totalBytes != fileSize) {
            n = dis.read(buffer, 0, buffer.length);
            fos.write(buffer, 0, n);
            fos.flush();
            totalBytes += n;
        }
        fos.close();
    }

    public String uploadCommand(File fileToUpload, String command) throws IOException {
        out.println(command);
        FileInputStream fis = new FileInputStream(fileToUpload);
        byte[] buffer = new byte[2048];
        int bytesRead;

        // Read the image data from the client and write it to the file
        while ((bytesRead = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, bytesRead);
            dos.flush();
        }
        fis.close();

        return in.readLine();
    }
}
