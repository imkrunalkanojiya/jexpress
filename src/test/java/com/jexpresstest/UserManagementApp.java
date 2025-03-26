package com.jexpresstest;

import com.google.gson.Gson;
import com.jexpress.JExpress;
import com.jexpress.Router;
import com.jexpress.utils.StatusCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Example application demonstrating the REST API framework with improved routing.
 */
public class UserManagementApp {
    private static final Logger LOGGER = Logger.getLogger(UserManagementApp.class.getName());

    // Simple User class
    static class User {
        private int id;
        private String name;
        private String email;

        public User(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    public static void main(String[] args) {
        // Create in-memory user list
        List<User> users = new ArrayList<>();
        users.add(new User(1, "John Doe", "john@example.com"));
        users.add(new User(2, "Jane Smith", "jane@example.com"));

        // Create Gson for JSON serialization
        Gson gson = new Gson();

        // Create application
        JExpress app = JExpress.create();
        Router router = app.getRouter();

        // GET all users
        router.get("/users", (req, res) -> {
            LOGGER.info("Fetching all users");
            res.json(gson.toJson(users));
        });

        // GET user by ID with more flexible path matching
        router.get("/users/{id}", (req, res) -> {
            try {
                int id = Integer.parseInt(req.getPathParam("id"));
                LOGGER.info("Fetching user with ID: " + id);

                User user = users.stream()
                        .filter(u -> u.id == id)
                        .findFirst()
                        .orElse(null);

                if (user != null) {
                    res.json(gson.toJson(user));
                } else {
                    res.setStatusCode(StatusCodes.NOT_FOUND);
                    res.json(gson.toJson("User not found"));
                }
            } catch (NumberFormatException e) {
                res.setStatusCode(StatusCodes.BAD_REQUEST);
                res.json(gson.toJson("Invalid user ID"));
            }
        });

        // POST new user
        router.post("/users", (req, res) -> {
            try {
                LOGGER.info("Received user creation request");
                User newUser = gson.fromJson(req.getBody(), User.class);

                // Simple validation
                if (newUser.name == null || newUser.email == null) {
                    res.setStatusCode(StatusCodes.BAD_REQUEST);
                    res.json(gson.toJson("Name and email are required"));
                    return;
                }

                // Assign new ID
                newUser.id = users.size() + 1;
                users.add(newUser);

                res.setStatusCode(StatusCodes.CREATED);
                res.json(gson.toJson(newUser));
            } catch (Exception e) {
                LOGGER.severe("Error creating user: " + e.getMessage());
                res.setStatusCode(StatusCodes.BAD_REQUEST);
                res.json(gson.toJson("Invalid user data"));
            }
        });

        // Optional: Add a health check endpoint
        router.get("/health", (req, res) -> {
            LOGGER.info("Health check endpoint accessed");
            res.json(gson.toJson("{\"status\": \"healthy\"}"));
        });

        // Start the server on port 8080
        LOGGER.info("Starting User Management Application");
        app.listen(8080).start();
    }
}