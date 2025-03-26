package com.jexpress.utils;

/**
 * Utility class for HTTP status codes.
 */
public final class StatusCodes {
    // 1xx Informational
    public static final int CONTINUE = 100;
    public static final int SWITCHING_PROTOCOLS = 101;

    // 2xx Success
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NO_CONTENT = 204;

    // 3xx Redirection
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int NOT_MODIFIED = 304;

    // 4xx Client Errors
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int NOT_ACCEPTABLE = 406;

    // 5xx Server Errors
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;

    private StatusCodes() {
        // Prevent instantiation
    }

    /**
     * Get the standard message for a given status code.
     *
     * @param statusCode The HTTP status code
     * @return The corresponding status message
     */
    public static String getMessage(int statusCode) {
        switch (statusCode) {
            // 1xx
            case CONTINUE: return "Continue";
            case SWITCHING_PROTOCOLS: return "Switching Protocols";

            // 2xx
            case OK: return "OK";
            case CREATED: return "Created";
            case ACCEPTED: return "Accepted";
            case NO_CONTENT: return "No Content";

            // 3xx
            case MOVED_PERMANENTLY: return "Moved Permanently";
            case FOUND: return "Found";
            case NOT_MODIFIED: return "Not Modified";

            // 4xx
            case BAD_REQUEST: return "Bad Request";
            case UNAUTHORIZED: return "Unauthorized";
            case FORBIDDEN: return "Forbidden";
            case NOT_FOUND: return "Not Found";
            case METHOD_NOT_ALLOWED: return "Method Not Allowed";
            case NOT_ACCEPTABLE: return "Not Acceptable";

            // 5xx
            case INTERNAL_SERVER_ERROR: return "Internal Server Error";
            case NOT_IMPLEMENTED: return "Not Implemented";
            case BAD_GATEWAY: return "Bad Gateway";
            case SERVICE_UNAVAILABLE: return "Service Unavailable";

            default: return "Unknown Status";
        }
    }
}