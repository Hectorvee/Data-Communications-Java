package csc2b.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ZEDEMHandler implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private DataOutputStream dos;

    public ZEDEMHandler(Socket newConnectionToClient) {
        socket = newConnectionToClient;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void run() {
        // Process commands from client

        try {
            String command = in.readLine();

            while (!command.equals("ZEDEMBYE")) {
                System.out.println("Message received");

                if (command.startsWith("BONJOUR")) {

                    String username = command.split(" ")[1];
                    String password = command.split(" ")[2];

                    if (matchUser(username, password)) {
                        out.println("JA Login Successful");
                    } else {
                        out.println("NEE login Unsuccessful. Please try again.");
                    }

                } else if (command.equals("PLAYLIST")) {
                    String joinedList = String.join("|", getFileList());
                    out.println("JA " + joinedList);

                } else if (command.startsWith("ZEDEMGET")) {
                    out.println(sendFile(command));
                } else {
                    out.println("NEE invalid command");
                }

                // Read the next command
                command = in.readLine();
            }

            out.println("JA Logout successful");

        } catch (IOException e) {
            out.println("NEE Server Error: " + e.getMessage());
        }
    }

    private boolean matchUser(String username, String password) {
        boolean found = false;
        File userFile = new File("data/server/user.txt");
        try {
            //Code to search users.txt file for match with username and password
            Scanner scan = new Scanner(userFile);
            while (scan.hasNextLine() && !found) {
                String line = scan.nextLine();
                String lineSec[] = line.split("\\s");

                //***OMITTED - Enter code here to compare user***
                if (username.equals(lineSec[0]) && password.equals(lineSec[1])) found = true;

            }
            scan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return found;
    }

    private ArrayList<String> getFileList() {
        ArrayList<String> result = new ArrayList<String>();
        //Code to add list text file contents to the arraylist.
        File lstFile = new File("data/server/MP3List.txt");
        try {
            Scanner scan = new Scanner(lstFile);

            //***OMITTED - Read each line of the file and add to the arraylist***
            while (scan.hasNext()) {
                result.add(scan.nextLine());
            }

            scan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private String idToFile(String ID) {
        String result = "";
        //Code to find the file name that matches strID
        File lstFile = new File("data/server/MP3List.txt");
        try {
            Scanner scan = new Scanner(lstFile);
            String line = "";
            //***OMITTED - Read filename from file and search for filename based on ID***
            while (scan.hasNext()) {
                line = scan.nextLine();
                if (line.split(" ")[0].equals(ID)) {
                    result = line.split(" ", 2)[1];
                    break;
                }
            }

            scan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String sendFile(String command) {
        String requestStatus = "";

        String id = command.split(" ")[1];
        String fileName = idToFile(id);
        File fileToSend = new File("data/server/" + fileName);

        if (fileToSend.exists()) {

            try {
                // Send the file size
                out.println(fileToSend.length());
                out.flush();

                // Send file to the client
                FileInputStream fis = new FileInputStream(fileToSend);
                byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                    dos.flush();
                }

                fis.close();
                requestStatus = "JA Download Successful";

            } catch (IOException e) {
                requestStatus = "NEE Download Unsuccessful: " + e.getMessage();
            }
        } else {
            requestStatus = "JA File does not exist";
        }


        return requestStatus;
    }
}
