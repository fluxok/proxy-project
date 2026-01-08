// Entry point for the proxy server.
// Listens for client connections and dispatches them to a thread pool.
package src;

import java.net.*;
import java.util.concurrent.*;

public class ProxyServer {

    public static void main(String[] args) {

        // Proxy listening port (default: 8080)
        int PORT = args.length > 0 ? Integer.parseInt(args[0]) : 8080;

        // Fixed-size thread pool to handle concurrent clients
        ExecutorService threadPool = Executors.newFixedThreadPool(40);

        // Graceful shutdown on JVM termination (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nproxy server stopped.");
            threadPool.shutdown();
        }));

        // Start listening for client connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("proxy server started on port: " + PORT);

            while (true) {
                // Accept incoming client connection
                Socket clSocket = serverSocket.accept();

                // Prevent indefinite blocking on inactive clients
                clSocket.setSoTimeout(10_000);

                // Handle client in thread pool
                threadPool.execute(() -> {
                    try {
                        new ClientHandler(clSocket).run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure thread pool shutdown on fatal errors
            threadPool.shutdown();
        }
    }
}
