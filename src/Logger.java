package src;

import java.io.*;
import java.time.*;

public class Logger {

    // Appends a single log entry to logs/proxy.log
    // Synchronized to avoid interleaved writes from multiple threads
    public static synchronized void log(String msg) {
        try (FileWriter fw = new FileWriter("logs/proxy.log", true)) {
            fw.write(LocalDateTime.now() + " " + msg + "\n");
        } catch (IOException ignored) {
            // Logging failures should not break proxy operation
        }
    }
}
