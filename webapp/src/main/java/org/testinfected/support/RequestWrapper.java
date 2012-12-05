package org.testinfected.support;

public class RequestWrapper implements Request {
    private final Request request;

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

    public void setAttribute(Object key, Object value) {
        request.setAttribute(key, value);
    }

    public void removeAttribute(Object key) {
        request.removeAttribute(key);
    }
}
