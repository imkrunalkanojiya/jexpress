package com.jexpress;

import com.jexpress.utils.StatusCodes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class for the REST API framework.
 */
public class JExpress {
    private static final Logger LOGGER = Logger.getLogger(JExpress.class.getName());

    private Router router;
    private int port;
    private ExecutorService threadPool;
    private volatile boolean isRunning;

    public JExpress() {
        this.router = new Router();
        this.port = 3000; // default port
        this.threadPool = Executors.newCachedThreadPool();
        this.isRunning = false;
    }

    /**
     * Set the port for the server to listen on.
     *
     * @param port Port number
     * @return Application instance for method chaining
     */
    public JExpress listen(int port) {
        this.port = port;
        return this;
    }

    /**
     * Get the router for adding routes.
     *
     * @return Router instance
     */
    public Router getRouter() {
        return router;
    }

    /**
     * Start the server and begin accepting connections.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            isRunning = true;
            LOGGER.info("Server started on port " + port);

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> handleRequest(clientSocket));
                } catch (IOException e) {
                    if (isRunning) {
                        LOGGER.log(Level.SEVERE, "Error accepting client connection", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server startup error", e);
        }
    }

    /**
     * Stop the server gracefully.
     */
    public void stop() {
        isRunning = false;
        threadPool.shutdown();
        LOGGER.info("Server stopped");
    }

    /**
     * Handle an individual client request.
     *
     * @param clientSocket Socket for the client connection
     */
    private void handleRequest(Socket clientSocket) {
        try {
            // Set a timeout to prevent hanging on incomplete requests
            clientSocket.setSoTimeout(5000);

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                // Check if there's data available to read
                if (!in.ready()) {
                    LOGGER.info("Received empty or incomplete request");
                    return;
                }

                // Parse request
                Request request = parseRequest(in);
                Response response = new Response();

                // Find and handle route
                Router.Route route = router.findRoute(request.getMethod(), request.getPath());

                if (route != null) {
                    route.handle(request, response);
                    sendResponse(out, response);
                } else {
                    // No route found
                    response.setStatusCode(StatusCodes.NOT_FOUND);
                    response.setBody("404 Not Found");
                    sendResponse(out, response);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Request handling error: " + e.getMessage(), e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing client socket", e);
            }
        }
    }

    /**
     * Parse the HTTP request from the input stream.
     *
     * @param in BufferedReader containing the request
     * @return Parsed Request object
     * @throws IOException If there's an error reading the request
     */
    private Request parseRequest(BufferedReader in) throws IOException {
        Request request = new Request();

        // Read the first line (request line)
        String requestLine = in.readLine();
        if (requestLine == null || requestLine.trim().isEmpty()) {
            throw new IOException("Empty or invalid request line");
        }

        // Parse request line
        String[] parts = requestLine.split(" ");
        if (parts.length < 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        // Set method
        try {
            request.setMethod(HttpMethod.valueOf(parts[0]));
        } catch (IllegalArgumentException e) {
            throw new IOException("Unsupported HTTP method: " + parts[0]);
        }

        // Set path
        request.setPath(parts[1]);

        // Read headers
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            int colonIndex = headerLine.indexOf(":");
            if (colonIndex > 0) {
                String headerName = headerLine.substring(0, colonIndex).trim();
                String headerValue = headerLine.substring(colonIndex + 1).trim();
                request.addHeader(headerName, headerValue);
            }
        }

        // Check for content length to read body
        String contentLength = request.getHeader("Content-Length");
        if (contentLength != null && !contentLength.isEmpty()) {
            try {
                int length = Integer.parseInt(contentLength);
                if (length > 0) {
                    char[] bodyChars = new char[length];
                    int charsRead = in.read(bodyChars, 0, length);
                    if (charsRead > 0) {
                        request.setBody(new String(bodyChars, 0, charsRead));
                    }
                }
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid Content-Length header: " + contentLength);
            }
        }

        return request;
    }

    /**
     * Send the HTTP response back to the client.
     *
     * @param out PrintWriter to send the response
     * @param response Response object to send
     */
    private void sendResponse(PrintWriter out, Response response) {
        // Write status line
        out.println("HTTP/1.1 " + response.getStatusCode() + " " +
                com.jexpress.utils.StatusCodes.getMessage(response.getStatusCode()));

        // Write headers
        out.println("Content-Type: " + response.getContentType());
        out.println("Content-Length: " + (response.getBody() != null ? response.getBody().length() : 0));
        out.println("Connection: close"); // Explicitly close connection

        // Write additional headers
        for (var entry : response.getHeaders().entrySet()) {
            out.println(entry.getKey() + ": " + entry.getValue());
        }

        // End of headers
        out.println();

        // Write body
        if (response.getBody() != null) {
            out.println(response.getBody());
        }
    }

    /**
     * Create and start a new REST API application.
     *
     * @return New Application instance
     */
    public static JExpress create() {
        return new JExpress();
    }
}