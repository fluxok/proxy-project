// Forwards HTTP requests to the destination server
// Relays the server response back to the client
package src;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestForwarder {

    // Forwards a parsed HTTP request and streams back the response
    public static void forward(HttpRequest req, Socket clientSocket)
            throws IOException {

        // Connect to destination server
        try (Socket serverSocket = new Socket(req.host, req.port)) {

            OutputStream serverOut = serverSocket.getOutputStream();
            InputStream serverIn = serverSocket.getInputStream();
            OutputStream clientOut = clientSocket.getOutputStream();

            // Write request headers using HTTP-compliant encoding
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(serverOut, StandardCharsets.ISO_8859_1)
            );

            // Write request line (origin-form)
            writer.write(req.method + " " + req.path + " " + req.httpVersion + "\r\n");

            // Forward all request headers
            for (Map.Entry<String, String> h : req.headers.entrySet()) {
                writer.write(h.getKey() + ": " + h.getValue() + "\r\n");
            }
            writer.write("\r\n");
            writer.flush();

            // Forward request body if present
            if (req.body != null) {
                serverOut.write(req.body);
                serverOut.flush();
            }

            // Stream server response back to client
            SocketRelay.relay(serverIn, clientOut);
        }
    }
}
