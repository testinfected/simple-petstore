package org.testinfected.support;

public interface Request {

    String protocol();

    String method();

    String uri();

    String getParameter(String name);

    <T> T unwrap(Class<T> type);

    String ip();

    Object attribute(Object key);

    void setAttribute(Object key, Object value);

    void removeAttribute(Object key);
}
