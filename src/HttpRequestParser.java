// Parses raw HTTP requests from a client socket
package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequestParser {

    // Reads and parses a single HTTP request
    public static HttpRequest parse(Socket clientSocket)
            throws IOException {

        InputStream in = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // Read request line: METHOD target HTTP/version
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) return null;

        HttpRequest req = new HttpRequest();
        req.method = parts[0];
        req.target = parts[1];
        req.httpVersion = parts[2];

        // Read HTTP headers
        Map<String, String> headers = new LinkedHashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(':');
            if (idx > 0) {
                headers.put(
                    line.substring(0, idx).trim(),
                    line.substring(idx + 1).trim()
                );
            }
        }
        req.headers = headers;

        // Resolve destination host, port, and path
        resolveHostAndPath(req);

        // Read request body if Content-Length is present
        if (headers.containsKey("Content-Length")) {
            int len = Integer.parseInt(headers.get("Content-Length"));
            req.body = in.readNBytes(len);
        }

        return req;
    }

    // Extracts host, port, and path from request target and headers
    private static void resolveHostAndPath(HttpRequest req) {

        // Absolute URI (proxy-style request)
        if (req.target.startsWith("http://")) {
            URI uri = URI.create(req.target);
            req.host = uri.getHost();
            req.path = uri.getRawPath() == null ? "/" : uri.getRawPath();
            if (uri.getPort() != -1) req.port = uri.getPort();
        }
        // Relative path with Host header
        else {
            String hostHeader = req.headers.get("Host");
            if (hostHeader == null) return;

            if (hostHeader.contains(":")) {
                String[] parts = hostHeader.split(":");
                req.host = parts[0];
                req.port = Integer.parseInt(parts[1]);
            } else {
                req.host = hostHeader;
            }
            req.path = req.target;
        }

        // Default HTTP port
        if (req.port == 0) req.port = 80;
    }
}
