import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class contains a main method that tests for open ports on the localhost
 * and displays the current computer's IP address. The application tests if a
 * connection is possible with every third port number starting from 1.
 */
public class Main {

    /**
     * The main method that starts the port scanning and displays the IP address.
     * @param args Command line arguments (not used in this program).
     */
    public static void main(String[] args) {


        /*
        Loop through port numbers from 1 to 65535, incrementing by 3 each time
        Attempt to create a socket connection to the specified port on localhost
        Display the local port of the connection
        Display the remote port of the connection
        Close the socket after the connection is successful and information is printed
         */
        for (int i = 1; i <= 65535; i += 3) {
            Socket socket = null;
            try {
                socket = new Socket("localhost", i);
                System.out.println("Program connected to localhost port: " + i);
                System.out.println("Local port of the connection: " + socket.getLocalPort());
                System.out.println("Remote port of the connection: " + socket.getPort());
                socket.close();
            } catch (UnknownHostException ex) {
//                System.err.println(ex);
            } catch (IOException ex) {
//                System.out.println("Could not connect to localhost port: " + i);
            } finally {
                // Ensure the socket is closed in the final block in case of an exception
                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Print the computer's IP address
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("The computer IP Address is: " + address.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
