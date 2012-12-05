package org.testinfected.support;

import org.simpleframework.http.Request;
import org.testinfected.petstore.ExceptionImposter;

import java.io.IOException;

public class SimpleRequest implements org.testinfected.support.Request {

    private final Request request;

    public SimpleRequest(Request request) {
        this.request = request;
    }

    public String protocol() {
        return String.format("HTTP/%s.%s", request.getMajor(), request.getMinor());
    }

    public String uri() {
        return request.getTarget();
    }

    public String method() {
        return request.getMethod();
    }

    public String getParameter(String name) {
        try {
            return request.getParameter(name);
        } catch (IOException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public <T> T unwrap(Class<T> type) {
        if (!type.isAssignableFrom(request.getClass())) throw new IllegalArgumentException("Unsupported type: " + type.getName());
        return type.cast(request);
    }

    public String ip() {
        return request.getClientAddress().getAddress().getHostAddress();
    }
}
