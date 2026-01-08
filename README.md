# Custom Network Proxy Server (PS)

This project implements a **Custom Network Proxy Server (PS)** in Java.  
The proxy server works as an intermediary between a client and a destination server. It receives client requests, applies basic rules, forwards the traffic, and returns the response.

The implementation is kept simple and clear for learning and academic use.

---

## Demo Video

Click the image below to watch a short demonstration of the Custom Network Proxy Server handling HTTP and HTTPS requests:


[![Proxy Server Demo](https://img.youtube.com/vi/gDESLKucwMI/0.jpg)](https://youtu.be/gDESLKucwMI)

---

## What the Proxy Does

- Accepts client connections on a fixed port
- Forwards normal HTTP requests
- Creates tunnels for HTTPS connections (CONNECT method)
- Blocks requests to selected domains
- Logs basic request information

---

## Project Structure (proxy-project)

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

## File Overview

- `ProxyServer.java`  
  Starts the proxy server and accepts client connections.

- `ClientHandler.java`  
  Handles a single client request from start to end.

- `HttpRequestParser.java` / `HttpRequest.java`  
  Parses incoming HTTP requests and stores request data.

- `RequestForwarder.java`  
  Forwards HTTP requests to the destination server.

- `HttpsTunnel.java`  
  Handles HTTPS connections using TCP tunneling.

- `SocketRelay.java`  
  Relays raw data between client and server sockets.

- `DomainFilter.java`  
  Blocks requests based on domain names.

- `Logger.java`  
  Writes simple logs for requests and blocked access.

---

## How to Run

1. Compile the source files:
   ```bash
   javac src/*.java

2. Run the proxy server:
   ```bash
   java src.ProxyServer

---

## Notes

- Blocked domains are listed in `config/blocked_domains.txt`
- Logs are written to `logs/proxy.log`
- This proxy is not intended for production use
