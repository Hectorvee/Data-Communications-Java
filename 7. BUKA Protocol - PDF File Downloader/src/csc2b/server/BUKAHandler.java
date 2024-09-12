package csc2b.server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class BUKAHandler implements Runnable
{
    public BUKAHandler(Socket newConnectionToClient)
    {	
	//Bind streams
    }
    
    public void run()
    {
	//Process commands from client		    
    }
    
    private boolean matchUser(String username,String password)
    {
	boolean found = false;
	File userFile = new File(""/*OMITTED - Enter file location*/);
	try
	{
		//Code to search users.txt file for match with username and password
	    Scanner scan = new Scanner(userFile);
	    while(scan.hasNextLine()&&!found)
	    {
		String line = scan.nextLine();
		String lineSec[] = line.split("\\s");
    		
		//***OMITTED - Enter code here to compare user*** 
		
	    }
	    scan.close();
	}
	catch(IOException ex)
	{
	    ex.printStackTrace();
	}
	
	return found;
    }
    
    private ArrayList<String> getFileList()
    {
		ArrayList<String> result = new ArrayList<String>();
		//Code to add list text file contents to the arraylist.
		File lstFile = new File(""/*OMITTED - Enter file location*/);
		try
		{
			Scanner scan = new Scanner(lstFile);

			//***OMITTED - Read each line of the file and add to the arraylist***
			
			scan.close();
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return result;
    }
    
    private String idToFile(String ID)
    {
    	String result = "";
    	//Code to find the file name that matches strID
    	File lstFile = new File(""/*OMITTED - Enter file location*/);
    	try
    	{
    		Scanner scan = new Scanner(lstFile);
    		String line = "";
    		//***OMITTED - Read filename from file and search for filename based on ID***
    		
    		scan.close();
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	return result;
    }
}
