package csc2b.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class BUKAHandler implements Runnable {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private DataOutputStream dos;

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
    
    public void run() {
	//Process commands from client

        try {

			String command = in.readLine().trim();

			while (!command.equals("LOGOUT")) {
				System.out.println("Message received");

				if (command.startsWith("AUTH")) {
					String username = command.split(" ")[1];
					String password = command.split(" ")[2];

					if (matchUser(username, password)) {
						out.println(statusCommand(200, "Login Successful\n"));
					} else {
						out.println(statusCommand(500, "Login Unsuccessful\n"));
					}

				} else if (command.startsWith("LIST")) {
					String response = statusCommand(200, String.join("|", getFileList()));
					out.println(response + "\n");
				} else if (command.startsWith("PDFRET")) {
					// Code for retrieving
				} else {
					out.println("400 Unknown command\n");
				}

				out.flush();
				command = in.readLine().trim();
			}

			out.println(statusCommand(200, " Logout successful\n"));


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
    
    private boolean matchUser(String username,String password) {
	boolean found = false;
	File userFile = new File("data/server/users.txt");
	try {
		//Code to search users.txt file for match with username and password
	    Scanner scan = new Scanner(userFile);
	    while(scan.hasNextLine()&&!found) {
		String line = scan.nextLine();
		String lineSec[] = line.split("\\s");
    		
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
    
    private ArrayList<String> getFileList() {
		ArrayList<String> result = new ArrayList<String>();
		//Code to add list text file contents to the arraylist.
		File lstFile = new File("data/server/pdfList.txt");
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
				if (line.equals(lineID)) {
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

	private String statusCommand(int errorCode, String message) {
		return errorCode + " " + message;
	}
}
