package org.testinfected.molecule.simple;

import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;
import org.testinfected.molecule.HttpException;
import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.SessionTracking;

import java.io.IOException;

public class SimpleRequest implements org.testinfected.molecule.Request {

    private final Request request;
    private final SessionTracking sessionTracking;

    public SimpleRequest(Request request, SessionTracking sessionTracking) {
        this.request = request;
        this.sessionTracking = sessionTracking;
    }

    public String protocol() {
        return String.format("HTTP/%s.%s", request.getMajor(), request.getMinor());
    }

    public String uri() {
        return request.getTarget();
    }

    public String pathInfo() {
        return request.getPath().getPath();
    }

    public HttpMethod method() {
        return HttpMethod.valueOf(request.getMethod());
    }

    public String parameter(String name) {
        try {
            return request.getParameter(name);
        } catch (IOException e) {
            throw new HttpException("Cannot read request parameter", e);
        }
    }

    public String ip() {
        return request.getClientAddress().getAddress().getHostAddress();
    }

    public Object attribute(Object key) {
        return request.getAttribute(key);
    }

    @SuppressWarnings("unchecked")
    public void attribute(Object key, Object value) {
        request.getAttributes().put(key, value);
    }

    public void removeAttribute(Object key) {
        request.getAttributes().remove(key);
    }

    public String cookie(String name) {
        Cookie cookie = request.getCookie(name);
        return cookie != null ? cookie.getValue() : null;
    }

    public Session session() {
        return session(true);
    }

    public Session session(boolean create) {
        return sessionTracking.openSession(this, create);
    }

    public <T> T unwrap(Class<T> type) {
        if (!type.isAssignableFrom(request.getClass())) throw new IllegalArgumentException("Unsupported type: " + type.getName());
        return type.cast(request);
    }
}
