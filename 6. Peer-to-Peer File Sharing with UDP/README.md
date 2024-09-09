# Peer-to-Peer File Sharing with UDP

## Overview

This practical involves creating a UDP-based peer-to-peer file sharing system. The system consists of a client that can operate in two modes: Seeder mode and Leecher mode. This system allows clients to share and receive files using peer-to-peer communication.

## Modes

### Seeder Mode

In Seeder mode, the client provides the following functionalities:

1. **Add Files**: Allows the user to add files to a list of available files for sharing. Use a FileChooser to select files and display them in a List.
2. **Send File List**: Sends a list of available files (e.g., a numbered list) to a connected peer using a "LIST" command.
3. **Send File**: Sends the chosen file to a requesting peer using a "FILE" command.

### Leecher Mode

In Leecher mode, the client provides the following functionalities:

1. **Connect to Seeder**: Connects to a Seeder client using a specified host address and port number. Use TextFields to capture this information from the user and a Connect button to initiate the connection.
2. **Request File List**: Requests the list of available files from the Seeder peer using a "LIST" command. Display the available files in a List for the user to choose from.
3. **Request File**: Sends a request for a specific file (using the file's index) to the Seeder peer using a "FILE" command. A Retrieve button can be used for this purpose.
4. **Receive and Save File**: Receives the requested file from the Seeder peer and saves it locally.

## Key Components

- **Seeder Mode**:
    - FileChooser for adding files
    - List to display added files
    - "LIST" command to send file list
    - "FILE" command to send selected file

- **Leecher Mode**:
    - TextFields for host address and port number
    - Connect button to initiate connection
    - List to display files from Seeder
    - "LIST" command to request file list
    - "FILE" command to request specific file
    - Functionality to receive and save the file

## Usage

1. **Start the Client**: Upon startup, choose the mode (Seeder or Leecher).
2. **In Seeder Mode**:
    - Add files to share.
    - Send file list to connected peers.
    - Send requested files to peers.
3. **In Leecher Mode**:
    - Connect to a Seeder peer.
    - Request and choose files from Seeder.
    - Request and receive the chosen file.

## Notes

- Implement the client using UDP for communication.
- Ensure proper handling of file transmission and reception.
- Test the client in both Seeder and Leecher modes to verify functionality.