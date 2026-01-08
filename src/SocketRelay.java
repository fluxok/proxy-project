// Streams raw bytes between two sockets
// Used for HTTP responses and HTTPS tunnels
package src;

import java.io.*;

public class SocketRelay {

    // Continuously copies data from input to output stream
    public static void relay(InputStream in, OutputStream out)
            throws IOException {

        byte[] buffer = new byte[8192];
        int read;

        // Forward data until the input stream closes
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            out.flush();
        }
    }
}
