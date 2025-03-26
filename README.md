# JExpress

## Overview

This Java REST API Framework provides a lightweight, flexible solution for building web applications and microservices with a focus on simplicity and ease of use.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Core Components](#core-components)
- [Routing](#routing)
- [Request Handling](#request-handling)
- [Response Handling](#response-handling)
- [Error Handling](#error-handling)
- [Middleware](#middleware)
- [Examples](#examples)
- [Advanced Usage](#advanced-usage)
- [Contributing](#contributing)
- [License](#license)

## Features

### HTTP Method Support
- Full support for standard HTTP methods:
    - GET
    - POST
    - PUT
    - DELETE
    - PATCH
    - HEAD
    - OPTIONS

### Routing Capabilities
- Flexible path-based routing
- Dynamic path parameter support
- Route matching with regex patterns
- Nested route configurations

### Request Handling
- Comprehensive request parsing
- Header management
- Query parameter extraction
- Path parameter handling
- Body content processing

### Response Management
- Flexible response generation
- Multiple content type support
- Status code management
- Header manipulation

### Error Handling
- Centralized error management
- Customizable error responses
- Logging integration

### Performance
- Non-blocking I/O
- Concurrent request processing
- Lightweight design

## Architecture

### Core Components
1. **Application**
    - Main entry point for creating REST applications
    - Server configuration
    - Route registration

2. **Router**
    - Manages route definitions
    - Handles route matching
    - Supports dynamic routing

3. **Request**
    - Represents HTTP request
    - Provides access to:
        - Method
        - Path
        - Headers
        - Query parameters
        - Path parameters
        - Request body

4. **Response**
    - Represents HTTP response
    - Supports:
        - Status code setting
        - Header manipulation
        - Body content generation
        - Content type management

5. **HttpMethod**
    - Enum representing supported HTTP methods

6. **Utilities**
    - MimeTypes
    - StatusCodes

## Getting Started

### Installation

#### Maven Dependency
```xml
<dependency>
    <groupId>com.restapi</groupId>
    <artifactId>java-rest-api-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Basic Usage

```java
public class HelloWorldApp {
    public static void main(String[] args) {
        // Create application
        Application app = Application.create();
        Router router = app.getRouter();

        // Define routes
        router.get("/hello", (req, res) -> {
            res.json("{\"message\": \"Hello, World!\"}");
        });

        // Start server
        app.listen(8080).start();
    }
}
```

## Routing

### Route Types
- Static routes
- Dynamic routes with path parameters
- Query parameter support

### Route Examples

```java
// Basic GET route
router.get("/users", (req, res) -> {
    // Fetch and return users
});

// Route with path parameter
router.get("/users/{id}", (req, res) -> {
    String userId = req.getPathParam("id");
    // Fetch specific user
});

// POST route
router.post("/users", (req, res) -> {
    // Create new user
});
```

## Request Handling

### Accessing Request Information

```java
router.get("/example", (req, res) -> {
    // Get HTTP method
    HttpMethod method = req.getMethod();

    // Get path parameters
    String userId = req.getPathParam("id");

    // Get query parameters
    String searchQuery = req.getQueryParam("q");

    // Get headers
    String contentType = req.getHeader("Content-Type");

    // Get request body
    String body = req.getBody();
});
```

## Response Handling

### Response Methods

```java
router.get("/users", (req, res) -> {
    // Set status code
    res.setStatusCode(200);

    // Set content type
    res.setContentType("application/json");

    // Send JSON response
    res.json("{\"users\": []}");

    // Send plain text
    res.text("User list");

    // Add custom headers
    res.addHeader("X-Custom-Header", "Value");
});
```

## Error Handling

### Global Error Handling

```java
router.get("/users/{id}", (req, res) -> {
    try {
        // Business logic
    } catch (Exception e) {
        res.setStatusCode(500);
        res.json("{\"error\": \"Internal Server Error\"}");
    }
});
```

## Middleware

### Middleware Support
*Note: Comprehensive middleware support is planned for future releases*

### Basic Middleware Concept
```java
// Middleware-like functionality
router.get("/protected", (req, res) -> {
    // Authentication check
    if (!isAuthenticated(req)) {
        res.setStatusCode(401);
        res.json("{\"error\": \"Unauthorized\"}");
        return;
    }

    // Continue with request
});
```

## Advanced Usage

### Complex Routing

```java
router.get("/users", this::listUsers);
router.get("/users/{id}", this::getUser);
router.post("/users", this::createUser);
router.put("/users/{id}", this::updateUser);
router.delete("/users/{id}", this::deleteUser);
```

## Configuration

### Server Configuration

```java
Application app = Application.create()
    .listen(8080)     // Set port
    .enableCORS()     // Enable CORS (future feature)
    .start();
```

## Contributing

### How to Contribute
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

### Development Setup
- Java 11+
- Maven
- JUnit 5 for testing

## Performance Considerations
- Use connection pooling
- Implement caching mechanisms
- Optimize database queries
- Consider using reactive programming for I/O-bound tasks

## Limitations
- No built-in authentication
- Basic middleware support
- File upload requires extension
- No WebSocket support (planned)

## Future Roadmap
- Enhanced middleware support
- WebSocket integration
- Advanced authentication
- Comprehensive plugin system
- Improved performance optimizations

## License
MIT License

---

**Disclaimer**: This framework is designed for educational and lightweight use cases. For production-grade applications, consider enterprise frameworks like Spring Boot.
