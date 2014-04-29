package com.vtence.molecule;

public interface Request {

    String protocol();

    HttpMethod method();

    String uri();

    String pathInfo();

    String parameter(String name);

    <T> T unwrap(Class<T> type);

    String ip();

    Object attribute(Object key);

    void attribute(Object key, Object value);

    void removeAttribute(Object key);

    String cookie(String name);

    Session session();

    Session session(boolean create);
}
