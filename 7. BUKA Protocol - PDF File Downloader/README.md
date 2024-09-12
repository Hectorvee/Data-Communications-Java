# BUKA Protocol - PDF File Downloader

The University of Johannesburg has tasked the development of a networked client and server application. The application serves as a basic PDF file downloader utilizing the **BUKA protocol**. BUKA requires client login before any commands are processed. The server tracks available PDF files by storing each file and its corresponding ID in a text file. The BUKA protocol runs on port `2018`.

## BUKA Protocol

### Request Commands
The following commands are available in BUKA:

- **AUTH `<Name>` `<Password>`**  
  Allows the client to log in with a provided name and password.  
  Example: `AUTH Drizzy p455w0rd`.  
  The server validates the credentials. If they are incorrect, an error message is returned. Upon successful login, the client can issue further requests.

- **LIST**  
  Returns a list of available PDF files.  
  The server retrieves the available files from the text file and sends the list to the client.

- **PDFRET `<ID>`**  
  Requests a PDF file by ID.  
  The server validates the ID. If invalid, an error message is returned. If valid, a confirmation message is sent, including the file size, and the file is transmitted to the client.

- **LOGOUT**  
  Logs the client off from the server.

### Response Codes
The following response codes are used by the server:

- **200 `<Message>`**  
  Indicates successful command execution. The `<Message>` provides additional helpful information.

- **500 `<Message>`**  
  Indicates an unsuccessful command. The `<Message>` explains why the command failed.

## Server Implementation: BUKAServer Class
The `BUKAServer` class is responsible for binding to the BUKA port (`2018`) and listening for incoming client connections. The server is designed to handle multiple clients simultaneously. Each connected client is processed by the `BUKAHandler`.

## Command Handler: BUKAHandler Class
The `BUKAHandler` class is responsible for interpreting commands received from clients and managing client logins. Registered users are stored in a text file called `users.txt`. The handler processes the following commands:
- **Login (AUTH)**
- **List available files (LIST)**
- **Return a requested file (PDFRET)**
- **Logout (LOGOUT)**

## Client Implementation: BUKAClient and BUKAClientFrame Classes
- The `BUKAClient` class handles communication with the server using the BUKA protocol.
- The `BUKAClientFrame` class provides a user interface (UI) with buttons for each available command. Any downloaded PDF files are saved to the disk. Errors from the server are displayed to the client.

## How to Run the Project
1. Start the `BUKAServer` to begin listening for client connections on port `2018`.
2. Launch the `BUKAClientFrame` to interact with the server through a graphical interface.
3. Use the UI buttons to log in, list files, download a file by ID, or log out.

## Error Handling
Any errors returned by the server (such as incorrect login credentials or invalid file IDs) are displayed to the client using a response code with a message:
- **500 `<Message>`** - For failed commands.
- **200 `<Message>`** - For successful commands.
