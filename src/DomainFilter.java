// Loads and applies domain-based blocking rules
package src;

import java.nio.file.*;
import java.util.*;

public class DomainFilter {

    // Set of blocked domains loaded at startup
    private static final Set<String> blocked = new HashSet<>();

    // Read blocked domains from config file
    static {
        try {
            for (String line : Files.readAllLines(
                    Paths.get("config/blocked_domains.txt"))) {

                line = line.trim().toLowerCase();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    blocked.add(line);
                }
            }
        } catch (Exception ignored) {
            // Missing or unreadable config file disables blocking
        }
    }

    // Returns true if the host is blocked
    public static boolean isBlocked(String host) {
        host = host.toLowerCase();

        for (String blockedDomain : blocked) {
            if (host.equals(blockedDomain) ||
                host.endsWith("." + blockedDomain)) {
                return true;
            }
        }
        return false;
    }
}
