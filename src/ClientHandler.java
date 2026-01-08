// Handles a single client connection.
// Parses the request, applies filtering, and forwards traffic.
package src;

import java.net.Socket;

public class ClientHandler implements Runnable {

    // Client socket connected to the proxy
    private final Socket clientSocket;

    // Initialize handler with client socket
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    // Entry point for handling a client request
    @Override
    public void run() {
        try {
            // Parse incoming HTTP request
            HttpRequest request = HttpRequestParser.parse(clientSocket);
            if (request == null) return;

            // Block requests to forbidden domains
            if (DomainFilter.isBlocked(request.host)) {
                Logger.log(clientSocket.getInetAddress() + " BLOCKED " + request.host);
                clientSocket.getOutputStream().write("HTTP/1.1 403 Forbidden\r\nContent-Length: 0\r\n\r\n".getBytes());
                return;
            }

            // Handle HTTPS tunneling (CONNECT method)
            if ("CONNECT".equalsIgnoreCase(request.method)) {
                Logger.log(clientSocket.getInetAddress() + " CONNECT " + request.host);
                HttpsTunnel.handle(request, clientSocket);
                return;
            }

            // Log allowed HTTP request
            Logger.log(clientSocket.getInetAddress() + " " + request.method + " " + request.host);

            // Forward normal HTTP request
            RequestForwarder.forward(request, clientSocket);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close socket for non-CONNECT requests
            if (!clientSocket.isClosed()) {
                try { clientSocket.close(); }
                catch (Exception ignored) {}
            }
        }
    }
}
