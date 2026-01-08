// Holds the parsed representation of an HTTP request
package src;

import java.util.Map;

public class HttpRequest {

    // HTTP method (GET, POST, CONNECT, etc.)
    public String method;

    // Raw request target from request line
    public String target;

    // Path component sent to destination server
    public String path;

    // HTTP version (e.g., HTTP/1.1)
    public String httpVersion;

    // Destination host and port resolved from request
    public String host;
    public int port = 80;

    // Request headers and optional body payload
    public Map<String, String> headers;
    public byte[] body;
}
