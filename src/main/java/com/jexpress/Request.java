package com.jexpress;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request in the REST API framework.
 */
public class Request {
    private HttpMethod method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;
    private String body;
    private String contentType;

    public Request() {
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.pathParams = new HashMap<>();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void addQueryParam(String key, String value) {
        this.queryParams.put(key, value);
    }

    public String getQueryParam(String key) {
        return this.queryParams.get(key);
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

    public void addPathParam(String key, String value) {
        this.pathParams.put(key, value);
    }

    public String getPathParam(String key) {
        return this.pathParams.get(key);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}