# ZEDEM Protocol - Audio File Transfer Application

The ZEDEM Audio File Transfer Application is designed to facilitate the transfer of audio files between a client and a server using the **ZEDEM protocol**. Clients must log in before they can access the server's resources. The server manages available audio files and handles multiple clients concurrently. ZEDEM operates on port `2021`.

## ZEDEM Protocol

### Request Commands
The following commands are available in ZEDEM:

- **BONJOUR `<Name>` `<Password>`**  
  Allows the client to log in with a specified name and password.  
  Example: `BONJOUR User Pass235`.  
  The server validates the credentials. If the credentials are incorrect, an error message is returned. Upon successful login, the client can issue additional commands.

- **PLAYLIST**  
  Retrieves a list of available audio files.  
  The server fetches the list from `MP3List.txt` and sends it to the client.

- **ZEDEMGET `<ID>`**  
  Requests an audio file by its unique ID.  
  The server checks the validity of the ID. If invalid, an error message is returned. If valid, the server sends a confirmation message with the file size, followed by the audio file.

- **ZEDEMBYE**  
  Logs the client off from the server.

### Response Codes
The following response codes are used by the server:

- **JA `<Message>`**  
  Indicates successful command execution. The `<Message>` provides additional details.

- **NEE `<Message>`**  
  Indicates an unsuccessful command. The `<Message>` explains the reason for failure.

## Server Implementation: ZEDEMServer Class
The `ZEDEMServer` class binds to port `2021` and listens for incoming client connections. It supports multiple simultaneous clients, with each client connection managed by an instance of the `ZEDEMHandler` class.

## Command Handler: ZEDEMHandler Class
The `ZEDEMHandler` class processes commands received from clients and handles client authentication. User credentials are stored in `users.txt`. The handler manages the following commands:
- **Login (BONJOUR)**
- **List available files (PLAYLIST)**
- **Return a requested file (ZEDEMGET)**
- **Logoff (ZEDEMBYE)**

## Client Implementation: ZEDEMClient and ZEDEMClientPane Classes
- The `ZEDEMClient` class manages communication with the server using the ZEDEM protocol.
- The `ZEDEMClientPane` class provides a JavaFX-based graphical user interface (GUI) with buttons for each command. Downloaded audio files are saved to the disk. Any server errors are displayed to the client.

## How to Run the Project
1. Start the `ZEDEMServer` to listen for client connections on port `2021`.
2. Launch the `ZEDEMClient` to open the graphical interface for interacting with the server.
3. Use the UI buttons to log in, list available audio files, request a file by ID, or log off.

## Error Handling
Errors from the server (such as incorrect login credentials or invalid file IDs) are displayed to the client with the following response codes:
- **NEE `<Message>`** - For failed commands.
- **JA `<Message>`** - For successful commands.

---

Thank you for using the ZEDEM Audio File Transfer Application!