# SMTP Client Java Application

## Overview

This project involves creating a GUI-based Java application that functions as an SMTP client to send emails. The application will connect to an SMTP server using the SMTP protocol, allowing users to send emails through a graphical interface.

## Features

- **SMTP Server Configuration**: Users can input the SMTP server hostname and port number.
- **Email Details**: Users can enter the sender's and recipient's email addresses and the content of the email.
- **Send Email**: A button to send the email and display the status of the email sending process, including any errors.

## Requirements

- **Java Swing**: For the graphical user interface.
- **SMTP Protocol**: For sending emails (without using the JavaMail API).
- **Testing**: Use SMTP testing tools like SMTP Bucket or Papercut (optional).

## Implementation Steps

1. **GUI Design**:
    - Create a user interface with text fields for SMTP server details, sender and recipient email addresses, and a text area for the email content.
    - Add a send button to trigger the email sending process.
    - Include a status area to display the result of the email sending operation and any errors.

2. **SMTP Client**:
    - Implement the connection to the SMTP server.
    - Send email using SMTP commands.
    - Handle server responses and errors.

## Setup

1. **Create the Java Swing GUI**: Design the interface with the required components.
2. **Implement SMTP Client**: Write code to connect to the SMTP server and send the email using the SMTP protocol.
3. **Testing**: Test the application with SMTP testing tools if needed.

## Notes

- Ensure to follow the SMTP protocol specifications for correct email transmission.
- The use of the JavaMail API is prohibited; you must code the SMTP protocol from scratch.
