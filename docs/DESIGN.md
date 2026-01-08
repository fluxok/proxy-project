## 1. Overview

This project implements a **Custom Network Proxy Server (PS)** in Java.  
The proxy server listens for client connections, receives HTTP/HTTPS requests, applies basic domain filtering, and forwards traffic to the destination server. Responses from the server are sent back to the client through the proxy.

The design is intentionally simple and readable, focusing on correct proxy behavior as defined in the problem statement.

---

## 2. Folder Structure (proxy-project)

### src/

    ├── ProxyServer.java
    ├── ClientHandler.java
    ├── HttpRequestParser.java
    ├── HttpRequest.java
    ├── RequestForwarder.java
    ├── HttpsTunnel.java
    ├── SocketRelay.java
    ├── DomainFilter.java
    ├── Logger.java

### config/

    ├── blocked_domains.txt

### logs/

    ├── proxy.log

### docs/

    ├── DESIGN.md
    ├── README.md
---

## 3. Architecture

The proxy follows a **client–proxy–server** model.

- `ProxyServer` listens on a port and accepts client connections.
- Each client connection is handled independently.
- Requests are parsed, checked, and then forwarded to the target server.
- Responses flow back through the proxy to the client.

There is no caching or modification of content.

---

## 4. Concurrency Model

The proxy uses a **fixed thread pool**.

- A thread pool is created at startup.
- Each incoming client socket is handled by a separate task.

---

## 5. Request Processing Flow

1. Client connects to the proxy.
2. `ClientHandler` starts processing the connection.
3. `HttpRequestParser` reads and parses the request.
4. `DomainFilter` checks whether the target host is blocked.
5. Based on request type:
   - HTTP → forwarded normally.
   - HTTPS (CONNECT) → tunnel is created.
6. Data is relayed between client and server.
7. Connection is closed after completion.

---

## 6. HTTP Handling

- HTTP requests are parsed into an `HttpRequest` object.
- `RequestForwarder` opens a socket to the destination server.
- Request headers and body are forwarded as they are.
- The server response is streamed back to the client using `SocketRelay`.
- The proxy does not store or alter HTTP responses.

---

## 7. HTTPS (CONNECT) Handling

- HTTPS requests using the `CONNECT` method are detected.
- `HttpsTunnel` establishes a TCP connection to the target server.
- A `200 Connection Established` response is sent to the client.
- Bidirectional byte streams are relayed between client and server.
- TLS encryption remains end-to-end and untouched by the proxy.

---

## 8. Domain Filtering

- Blocked domains are listed in `config/blocked_domains.txt`.
- `DomainFilter` loads this list at startup.
- Requests to blocked domains return a `403 Forbidden` response or an empty line in terminal.
- Subdomains of blocked domains are also blocked (implemented using suffix matching).

---

## 9. Logging

- All important events are logged to `logs/proxy.log`.
- Logged information includes:
  - Client IP address
  - Request method
  - Target host
  - Blocked or allowed status

---

## 10. Limitations

- Uses blocking I/O and a simple thread pool.
- No caching or authentication support.
- No HTTPS inspection or TLS termination.
- Limited error handling for malformed requests.
- Not production use.
