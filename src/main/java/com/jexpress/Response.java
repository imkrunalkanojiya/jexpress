package com.jexpress;

import com.jexpress.utils.MimeTypes;
import com.jexpress.utils.StatusCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP response in the REST API framework.
 */
public class Response {
    private int statusCode;
    private String body;
    private Map<String, String> headers;
    private String contentType;

    public Response() {
        this.statusCode = StatusCodes.OK;
        this.headers = new HashMap<>();
        this.contentType = MimeTypes.APPLICATION_JSON;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Convenience method to set JSON content type.
     */
    public void json() {
        setContentType(MimeTypes.APPLICATION_JSON);
    }

    /**
     * Convenience method to set text/plain content type.
     */
    public void text() {
        setContentType(MimeTypes.TEXT_PLAIN);
    }

    /**
     * Convenience method to send a simple response.
     *
     * @param body Response body
     */
    public void send(String body) {
        this.body = body;
    }

    /**
     * Convenience method to send JSON response.
     *
     * @param jsonBody JSON response body
     */
    public void json(String jsonBody) {
        json();
        this.body = jsonBody;
    }
}