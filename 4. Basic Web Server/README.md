# Basic Web Server

## Overview

This project involves creating a basic web server that adheres to the HTTP protocol. The server will handle client connections in a multi-threaded manner and serve specific content based on client requests.

## Features

- **Port Listening**: The server listens for incoming connections on port 4321.
- **Multi-Threaded Handling**: The server is capable of handling multiple client connections simultaneously.
- **HTTP Protocol Compliance**: The server processes client requests according to the HTTP protocol and returns appropriate response codes.
- **Content Serving**: The server serves specific HTML and binary files based on the client's request.

## Response Codes

- **200 OK**: When a request can be served successfully. The server must handle binary files as well.
- **404 Not Found**: When the requested page or content cannot be found.
- **500 Internal Server Error**: When an error occurs on the server.

## File Handling

The server should handle requests for the following resources:

- **`localhost:4321/Joburg`**: Serve the `Joburg.html` file.
- **`localhost:4321/Durban`**: Serve the `Durban.html` file.
- **`localhost:4321/Cape`**: Serve the `CapeWithImage.html` file.
- **`localhost:4321/Africa.jpg`**: Serve the `Africa.jpg` file.

## Testing

- **Browser**: Use a web browser (Opera, Firefox, Chrome) to test the server on `localhost` with the appropriate port (4321).
- **Requests**: Verify that the server correctly serves the requested files and handles response codes as specified.

## Notes

- Ensure that the server correctly handles and serves both HTML and binary files.
- Implement multi-threading to manage multiple client connections efficiently.