# Chat Application

A real-time chat application built with Java Swing that allows two users to communicate over a local network. The application features a modern UI with message bubbles, timestamps, and status indicators.

## Features

- Real-time messaging between client and server
- Custom UI with:
  - Message bubbles with timestamps
  - User profile pictures
  - Status indicators
  - Custom background images
- Network communication using sockets
- Responsive design that works on different screen sizes
- Proper connection handling and cleanup

## Technologies Used
- Java 17
- Java Swing for GUI
- Java Networking (ServerSocket/Socket)
- DataInputStream/DataOutputStream for message serialization

## How It Works
- The Server starts listening on port 6001
- Client connects to the server at localhost:6001
- Users can exchange messages in real-time

## Known Issues
- The application currently only works on localhost
- No message history persistence
- No user authentication
