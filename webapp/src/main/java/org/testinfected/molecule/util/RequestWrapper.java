package org.testinfected.molecule.util;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Session;

public class RequestWrapper implements Request {
    protected final Request request;

    public RequestWrapper(Request request) {
        this.request = request;
    }

    public String protocol() {
        return request.protocol();
    }

    public String method() {
        return request.method();
    }

    public String uri() {
        return request.uri();
    }

    public String pathInfo() {
        return request.pathInfo();
    }

    public String parameter(String name) {
        return request.parameter(name);
    }

    public <T> T unwrap(Class<T> type) {
        return request.unwrap(type);
    }

    public String ip() {
        return request.ip();
    }

    public Object attribute(Object key) {
        return request.attribute(key);
    }

    public void attribute(Object key, Object value) {
        request.attribute(key, value);
    }

    public void removeAttribute(Object key) {
        request.removeAttribute(key);
    }

    public Session session() {
        return request.session();
    }
}
