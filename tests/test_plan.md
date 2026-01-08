## Overview

This document lists basic terminal commands used to test the Custom Network Proxy Server. All tests were performed manually using the Linux terminal.

---

## Starting the Proxy Server
The proxy server is started from the terminal after compiling the source files.

Command used:
```bash
    javac src/*.java
    java src/ProxyServer
```

---

## Basic HTTP  & HTTPS Test Using curl

### Unblocked HTTP (example.com)

Command used:
```bash
curl -x http://localhost:8080 http://example.com
```

---

### Unblocked HTTPS (wikipedia.org)

Command used:
```bash
curl -x http://localhost:8080 https://www.wikipedia.org
```

---

### Blocked HTTP (google.com)

Command used:
```bash
curl -x http://localhost:8080 http://www.google.com
```

---

### Blocked HTTPS (google.com)

Command used:
```bash
curl -x http://localhost:8080 https://www.google.com
```

--- 

## Basic Concurrency Test for HTTP and HTTPS
```bash
seq 1 10 | xargs -n1 -P10 curl -x http://localhost:8080 http://example.com
```

---

```bash
seq 1 10 | xargs -n1 -P10 curl -x https://localhost:8080 https://example.com
```

---

## Stopping the Server
```bash
Ctrl + C
```

---

Conclusion
The proxy server was tested using only terminal-based commands. All basic tests confirmed correctness.