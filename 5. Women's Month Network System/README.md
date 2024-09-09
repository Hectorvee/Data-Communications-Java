# Women's Month Network System

## Overview

In celebration of Women’s Month, the Academy of Computer Science and Software Engineering has tasked you with developing a network system for uploading and retrieving images of women who have impacted the lives of staff and students. This system includes both a server and a client component.

## Server

### Functionality

- **Port**: The server should run on port `5432`.
- **Image List**: The server will use a text file to store a list of image files with corresponding IDs.
- **Requests Handling**:
  - **LIST**: Returns a list of image file names and their IDs.
  - **DOWN `<ID>`**: Returns the requested image with the specified ID.
  - **UP `<ID>` `<Name>` `<Size>` `<Image>`**: Stores the uploaded image, updates the text file, and responds with "SUCCESS" if successful, otherwise "FAILURE".

### Implementation

- Implement a server that listens for connections on port `5432`.
- Use a text file to maintain a mapping of image IDs to image files.
- Handle client requests as specified above.

## Client

### Functionality

- **GUI**: Create a user-friendly graphical interface.
- **Upload Image**: Allow users to select and upload an image to the server.
- **Request List**: Enable users to request and view a list of available images.
- **Retrieve Image**: Allow users to enter an ID to retrieve and display the corresponding image from the server.

### Implementation

- Develop a GUI that provides:
  - **Upload**: Functionality to select and upload images.
  - **List**: Ability to request and display a list of images.
  - **Download**: Option to enter an ID and retrieve the associated image.

## Testing

- Use the provided data folder (under "Additional files") for testing purposes.

## Notes

- Ensure proper error handling and user feedback for both uploading and downloading operations.
- The server should be able to manage image storage and retrieval efficiently.