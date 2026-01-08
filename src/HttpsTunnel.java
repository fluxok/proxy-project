// Handles HTTPS connections using the CONNECT method
package src;

import java.io.*;
import java.net.*;

public class HttpsTunnel {

    // Establishes a TCP tunnel between client and target server
    public static void handle(HttpRequest req, Socket clientSocket)
            throws IOException {

        // Use resolved host and port from parsed request
        String host = req.host;
        int port = req.port;

        // Connect to destination server
        Socket serverSocket = new Socket(host, port);

        InputStream clientIn = clientSocket.getInputStream();
        OutputStream clientOut = clientSocket.getOutputStream();

        InputStream serverIn = serverSocket.getInputStream();
        OutputStream serverOut = serverSocket.getOutputStream();

        // Inform client that tunnel is ready
        clientOut.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
        clientOut.flush();

        // Forward data from client to server
        Thread t1 = new Thread(() -> {
            try {
                SocketRelay.relay(clientIn, serverOut);
            } catch (IOException ignored) {}
        });

        // Forward data from server to client
        Thread t2 = new Thread(() -> {
            try {
                SocketRelay.relay(serverIn, clientOut);
            } catch (IOException ignored) {}
        });

        t1.start();
        t2.start();

        // Wait until tunnel closes
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {
        } finally {
            closeQuietly(serverSocket);
            closeQuietly(clientSocket);
        }
    }

    // Safely closes a socket
    private static void closeQuietly(Socket s) {
        try {
            if (!s.isClosed()) s.close();
        } catch (IOException ignored) {}
    }
}
