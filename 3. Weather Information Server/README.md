# Weather Information Server

## Overview

This project involves developing a simple server application that provides weather information based on client requests. The server uses a custom protocol to handle incoming connections and respond to weather queries. It is designed to interact with clients using a specified format for requests and responses.

## Features

- **Server Initialization**: The server listens on port 8888 (or another suitable port for testing) and displays "Ready for incoming connections..." when started.
- **Connection Greeting**: Upon client connection, the server sends "HELLO - you may make 4 requests and I’ll provide weather information".
- **Session Management**: The server allows a client to make up to 4 requests before sending a "GOOD BYE - [4] queries answered" message and closing the connection.
- **Request Handling**: Clients can send weather requests in the format "REQUEST [location]". The server responds based on the location specified:
    - **Johannesburg**: "Clear Skies in Joburg"
    - **Durban**: "Sunny and Warm in Durban"
    - **Cape Town**: "Cool and Cloudy in Cape Town"
    - **Other Locations**: Randomly respond with "No data available", "Please try again later", or "[Location] data outdated".
- **Session Termination**: Clients can end the session by sending a "DONE" message. The server will respond with "0# GOOD BYE - [x] queries answered" and terminate the connection.

## Server Behavior

1. **Startup**: The server starts and displays "Ready for incoming connections...".
2. **Client Connection**: On client connection, the server sends a greeting message: "HELLO - you may make 4 requests and I’ll provide weather information".
3. **Session Start**: The client sends a "START" message, and the server responds with "REQUEST or DONE".
4. **Weather Requests**: The client sends "REQUEST [location]" messages. The server responds with weather information or an error message based on the location.
5. **Session End**: The client can send a "DONE" message to end the session. The server responds with "0# GOOD BYE - [x] queries answered" and closes the connection.

## Testing

- **Testing Tool**: Use PuTTY or another suitable tool to interact with the server and test the protocol.
- **Port**: The server should be tested on port 8888 or another chosen port.

## Notes

- Ensure that the server correctly handles up to 4 requests and properly manages session termination.
- Implement the custom protocol as specified to ensure proper interaction between the server and client.
