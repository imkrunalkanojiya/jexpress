package com.jexpress.utils;

/**
 * Utility class for handling MIME types.
 */
public final class MimeTypes {
    // Common MIME types
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    private MimeTypes() {
        // Prevent instantiation
    }

    /**
     * Get the MIME type based on file extension.
     *
     * @param filename The name of the file
     * @return The corresponding MIME type
     */
    public static String getContentType(String filename) {
        if (filename == null) {
            return APPLICATION_OCTET_STREAM;
        }

        filename = filename.toLowerCase();

        if (filename.endsWith(".json")) {
            return APPLICATION_JSON;
        } else if (filename.endsWith(".xml")) {
            return APPLICATION_XML;
        } else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
            return TEXT_HTML;
        } else if (filename.endsWith(".txt")) {
            return TEXT_PLAIN;
        }

        return APPLICATION_OCTET_STREAM;
    }
}