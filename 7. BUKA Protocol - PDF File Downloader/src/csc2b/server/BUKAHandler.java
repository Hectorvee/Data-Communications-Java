package csc2b.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles communication with a connected client.
 * Implements the Runnable interface to handle client requests in a separate thread.
 */
public class BUKAHandler implements Runnable {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private DataOutputStream dos;

	/**
	 * Constructor that initializes the client connection and binds input/output streams.
	 *
	 * @param newConnectionToClient The socket connected to the client.
	 */
    public BUKAHandler(Socket newConnectionToClient) {
	//Bind streams
		this.clientSocket = newConnectionToClient;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
        }
    }

	/**
	 * Handles client commands in a loop until the client logs out.
	 * Supported commands: AUTH, LIST, PDFRET, LOGOUT.
	 */
    public void run() {
	//Process commands from client

        try {
			String command = in.readLine().trim();

			// Loop until the client sends the "LOGOUT" command
			while (!command.equals("LOGOUT")) {
				System.out.println("Message received");

				if (command.startsWith("AUTH")) {
					// Handle AUTH command (authenticate user)
					String username = command.split(" ")[1];
					String password = command.split(" ")[2];

					if (matchUser(username, password)) {
						out.println("200 Login Successful");
					} else {
						out.println("500 Login Unsuccessful");
					}

				} else if (command.startsWith("LIST")) {
					// Handle LIST command (return list of available files)
					String response = "200 " + String.join("|", getFileList());
					out.println(response);

				} else if (command.startsWith("PDFRET")) {
					// Handle PDFRET command (retrieve a file)
					out.println(sendFile(command));

				} else {
					// Handle unknown commands
					out.println("400 Unknown command");
				}

				out.flush();
				command = in.readLine().trim();	// Read next command
			}

			// Send logout response
			out.println("200 Logout successful");

        } catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
        } finally {

			try {
				if (clientSocket!=null && !clientSocket.isClosed()) {
					clientSocket.close();
				}

			} catch (IOException e) {
				System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

	/**
	 * Matches the provided username and password with the stored credentials.
	 *
	 * @param username The username provided by the client.
	 * @param password The password provided by the client.
	 * @return true if the credentials match; false otherwise.
	 */
    private boolean matchUser(String username,String password) {
	boolean found = false;
	File userFile = new File("data/server/users.txt");

	try {
		//Code to search users.txt file for match with username and password
	    Scanner scan = new Scanner(userFile);
	    while(scan.hasNextLine()&&!found) {
			String line = scan.nextLine();
			String[] lineSec = line.split("\\s");

			//***OMITTED - Enter code here to compare user***
			if (username.equals(lineSec[0]) && password.equals(lineSec[1])) {
				found = true;
			}
		
	    }
	    scan.close();
	}
	catch(IOException ex)
	{
	    ex.printStackTrace();
	}
	
	return found;
    }

	/**
	 * Retrieves the list of available PDF files.
	 *
	 * @return An ArrayList of file names available on the server.
	 */
    private ArrayList<String> getFileList() {
		ArrayList<String> result = new ArrayList<String>();
		//Code to add list text file contents to the arraylist.
		File lstFile = new File("data/server/PdfList.txt");
		try
		{
			Scanner scan = new Scanner(lstFile);

			//***OMITTED - Read each line of the file and add to the arraylist***
			while (scan.hasNextLine()) {
				result.add(scan.nextLine());
			}
			
			scan.close();
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return result;
    }

	/**
	 * Finds the file name associated with a given ID.
	 *
	 * @param ID The ID provided by the client to identify the file.
	 * @return The name of the file corresponding to the provided ID.
	 */
    private String idToFile(String ID) {
    	String result = "";
    	//Code to find the file name that matches strID
    	File lstFile = new File("data/server/PdfList.txt");
    	try
    	{
    		Scanner scan = new Scanner(lstFile);
    		String line = "";
    		//***OMITTED - Read filename from file and search for filename based on ID***
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				String lineID = line.split(" ")[0];
				if (lineID.equals(ID)) {
					result = line.split(" ")[1];
				}
			}
    		
    		scan.close();
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	return result;
    }

	/**
	 * Sends a file to the client.
	 *
	 * @param command The client's request command which includes the file ID.
	 * @return A status message indicating whether the file was successfully sent or not.
	 */
	private String sendFile(String command) {
		String downloadStatus = "";

		// Extract file ID from the command and map it to a file name
		String fileName = idToFile(command.split(" ")[1]);
		File fileToSend = new File("data/server/" + fileName);

		if (fileToSend.exists()) {

			try {
				// Send file size
				out.println(fileToSend.length());

				// Send file to the client
				FileInputStream fis = new FileInputStream(fileToSend);
				byte[] buffer = new byte[2048];
				int bytesRead;

				while ((bytesRead = fis.read(buffer)) != -1) {
					dos.write(buffer, 0, bytesRead);
					dos.flush();
				}

				fis.close();
				downloadStatus = "200 Success downloading file";

			} catch (IOException e) {
				downloadStatus = "Error: " + e.getMessage();
            }
        } else {
			downloadStatus = "File does not exist";
		}

		return downloadStatus;
	}
}
