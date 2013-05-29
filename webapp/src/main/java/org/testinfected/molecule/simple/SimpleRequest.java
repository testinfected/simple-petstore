package org.testinfected.molecule.simple;

import org.simpleframework.http.Request;
import org.simpleframework.util.lease.LeaseException;
import org.testinfected.molecule.HttpException;
import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.Session;

import java.io.IOException;

public class SimpleRequest implements org.testinfected.molecule.Request {

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

    public Session session() {
        return session(true);
    }

    public Session session(boolean create) {
        final org.simpleframework.http.session.Session session;
        try {
            session = request.getSession(create);
        } catch (LeaseException e) {
            throw new HttpException("Cannot acquire session", e);
        }

         return session != null ? new SimpleSession(session) : null;
    }

    public <T> T unwrap(Class<T> type) {
        if (!type.isAssignableFrom(request.getClass())) throw new IllegalArgumentException("Unsupported type: " + type.getName());
        return type.cast(request);
    }
}
