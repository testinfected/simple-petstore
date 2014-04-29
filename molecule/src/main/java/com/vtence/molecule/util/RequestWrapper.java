package com.vtence.molecule.util;

import com.vtence.molecule.HttpMethod;
import com.vtence.molecule.Request;
import com.vtence.molecule.Session;

public class RequestWrapper implements Request {
    protected final Request request;

    public RequestWrapper(Request request) {
        this.request = request;
    }

    public String protocol() {
        return request.protocol();
    }

    public HttpMethod method() {
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

    public String cookie(String name) {
        return request.cookie(name);
    }

    public Session session() {
        return request.session();
    }

    public Session session(boolean create) {
        return request.session(create);
    }
}
