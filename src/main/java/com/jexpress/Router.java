package com.jexpress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles routing for the REST API framework with enhanced path matching.
 */
public class Router {
    private List<Route> routes;

    public Router() {
        this.routes = new ArrayList<>();
    }

    /**
     * Add a route to the router.
     *
     * @param method HTTP method
     * @param path Route path
     * @param handler Route handler
     */
    public void addRoute(HttpMethod method, String path, BiConsumer<Request, Response> handler) {
        routes.add(new Route(method, path, handler));
    }

    /**
     * GET route shorthand method.
     *
     * @param path Route path
     * @param handler Route handler
     */
    public void get(String path, BiConsumer<Request, Response> handler) {
        addRoute(HttpMethod.GET, path, handler);
    }

    /**
     * POST route shorthand method.
     *
     * @param path Route path
     * @param handler Route handler
     */
    public void post(String path, BiConsumer<Request, Response> handler) {
        addRoute(HttpMethod.POST, path, handler);
    }

    /**
     * PUT route shorthand method.
     *
     * @param path Route path
     * @param handler Route handler
     */
    public void put(String path, BiConsumer<Request, Response> handler) {
        addRoute(HttpMethod.PUT, path, handler);
    }

    /**
     * DELETE route shorthand method.
     *
     * @param path Route path
     * @param handler Route handler
     */
    public void delete(String path, BiConsumer<Request, Response> handler) {
        addRoute(HttpMethod.DELETE, path, handler);
    }

    /**
     * Find a route that matches the request method and path.
     *
     * @param method HTTP method
     * @param path Request path
     * @return Matching route or null
     */
    public Route findRoute(HttpMethod method, String path) {
        for (Route route : routes) {
            if (route.matches(method, path)) {
                return route;
            }
        }
        return null;
    }

    /**
     * Inner class representing a single route with advanced path matching.
     */
    public static class Route {
        private HttpMethod method;
        private String pathTemplate;
        private Pattern pathPattern;
        private BiConsumer<Request, Response> handler;

        public Route(HttpMethod method, String pathTemplate, BiConsumer<Request, Response> handler) {
            this.method = method;
            this.pathTemplate = pathTemplate;
            this.handler = handler;
            this.pathPattern = compilePathPattern(pathTemplate);
        }

        /**
         * Compile path template to a regex pattern.
         *
         * @param pathTemplate Path template with optional {param} placeholders
         * @return Compiled regex Pattern
         */
        private Pattern compilePathPattern(String pathTemplate) {
            // Escape any regex special characters
            String regexPath = pathTemplate.replace(".", "\\.")
                    .replace("*", ".*");

            // Replace {param} with named capture groups
            regexPath = regexPath.replaceAll("\\{([^}]+)\\}", "(?<$1>[^/]+)");

            return Pattern.compile("^" + regexPath + "$");
        }

        /**
         * Check if this route matches the given method and path.
         *
         * @param method HTTP method
         * @param requestPath Request path
         * @return true if route matches, false otherwise
         */
        public boolean matches(HttpMethod method, String requestPath) {
            // First check the HTTP method
            if (this.method != method) {
                return false;
            }

            // Then check the path using regex
            Matcher matcher = pathPattern.matcher(requestPath);
            return matcher.matches();
        }

        /**
         * Extract path parameters from the request path.
         *
         * @param requestPath Request path
         * @return Map of path parameters
         */
        public Map<String, String> extractPathParams(String requestPath) {
            Map<String, String> pathParams = new HashMap<>();
            Matcher matcher = pathPattern.matcher(requestPath);

            if (matcher.matches()) {
                // Get parameter names from the original path template
                Pattern paramPattern = Pattern.compile("\\{([^}]+)\\}");
                Matcher paramMatcher = paramPattern.matcher(pathTemplate);

                while (paramMatcher.find()) {
                    String paramName = paramMatcher.group(1);
                    String paramValue = matcher.group(paramName);
                    if (paramValue != null) {
                        pathParams.put(paramName, paramValue);
                    }
                }
            }

            return pathParams;
        }

        /**
         * Handle the request by executing the route handler.
         *
         * @param request Request object
         * @param response Response object
         */
        public void handle(Request request, Response response) {
            // Add path parameters to the request
            Map<String, String> pathParams = extractPathParams(request.getPath());
            pathParams.forEach(request::addPathParam);

            handler.accept(request, response);
        }

        public HttpMethod getMethod() {
            return method;
        }

        public String getPath() {
            return pathTemplate;
        }
    }
}