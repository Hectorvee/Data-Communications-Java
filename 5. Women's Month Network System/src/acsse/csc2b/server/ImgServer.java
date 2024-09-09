package acsse.csc2b.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ImgServer {
    private static ServerSocket serverSocket;
    private static final int PORT = 5432;

    public static void main(String[] args) {
        System.out.println("Starting server...");

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        do {
            run();
        } while (true);
    }

    private static void run() {
        Socket link = null;

        try {
            // Accept the incoming connection
            link = serverSocket.accept();
            System.out.println("New connection made: " + link.getInetAddress().getHostAddress());

            // Set up streams for network communication
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));    // Input stream to receive data from the client
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);    // Output stream to send data to the client
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(link.getOutputStream()));    // Output stream to send files to the client
            DataInputStream dis = new DataInputStream(new BufferedInputStream(link.getInputStream()));   // Input stream to receive files from the client

                int numMessages = 0;
                String message = in.readLine().trim();

                while (!message.equals("***CLOSE***")) {
                    System.out.println("Message received");

                    if (message.startsWith("LIST")) {
                        // Code for listing images...
                        out.println("Message " + numMessages + ": " + listImages());
                        numMessages++;
                    } else if (message.startsWith("UP")) {
                        // Code for uploading image...
                        uploadImage(message, out, dis, numMessages);
                        numMessages++;

                    } else if (message.startsWith("DOWN")) {
                        // Code for downloading image...
                        downloadImage(message, out, dos, numMessages);


                    } else {
                        out.println("Message " + numMessages + ": " + "Command error");
                    }

                    // Get the next message
                    message = in.readLine().trim();
                }
                out.println(numMessages + " messages received");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                System.out.println("\n* Closing connection... *");
                link.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void uploadImage(String message, PrintWriter out, DataInputStream dis, int numMessages) {
        try {
            // Get the image ID, file name, and file size from the message
            String[] parts = message.split(" ");
            String imageID = parts[1];
            String fileName = parts[2];
            int fileSize = Integer.parseInt(parts[3]);

            // Create a new file to store the image
            File fileToUpload = new File("data/server/" + fileName);
            if (fileToUpload.exists()) {
                out.println("Message " + numMessages + ": " + "FAILURE");
                return;
            }

            FileOutputStream fos = new FileOutputStream(fileToUpload);

            // Create a buffer to store the image data
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;

            // Read the image data from the client and write it to the file
            while (totalBytes != fileSize) {
                bytesRead = dis.read(buffer, 0, buffer.length);
                fos.write(buffer, 0, bytesRead);
                fos.flush();
                totalBytes += bytesRead;
            }
            fos.close();

            // Add the image to the ImgList.txt file
            FileWriter fw = new FileWriter("data/server/ImgList.txt", true);
            fw.write(imageID + " " + fileName + "\n");
            fw.close();

            out.println("Message " + numMessages + ": " + "SUCCESS");
        } catch (IOException e) {
            out.println("Message " + numMessages + ": " + "FAILURE");
        }
    }

    private static String listImages() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("data/server/ImgList.txt"));
            return String.join(" | ", lines);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private static void downloadImage(String message, PrintWriter out, DataOutputStream dos, int numMessages) {
        // Get the image ID from the message
        String imageID = message.split(" ")[1];
        File fileToReturn = null;

        // Search for the image in the ImgList.txt file and get the file path
        fileToReturn = getFile(out, imageID, fileToReturn);

        if (fileToReturn.exists()) {

            // Send the file size to the client
            try {
                out.println(fileToReturn.length());

                // Send the file to the client
                FileInputStream fis = new FileInputStream(fileToReturn);
                byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, bytesRead);
                    dos.flush();
                }
                fis.close();
            } catch (IOException e) {
                out.println("Message " + numMessages + ": " + "Download Failed");
            }

        } else {
            out.println("Error: Image not found");
        }

    }

    private static File getFile(PrintWriter out, String imageID, File fileToReturn) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("data/server/ImgList.txt"));
            for (String line : lines) {
                String[] parts = line.split(" ");
                if (parts[0].equals(imageID)) {
                    fileToReturn = new File("data/server/" + parts[1]);
                }
            }
        } catch (IOException e) {
            out.println("Error: " + e.getMessage());
        }
        return fileToReturn;
    }

}
