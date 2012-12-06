package org.testinfected.support;

import org.simpleframework.util.lease.LeaseException;

public interface Request {

    String protocol();

    String method();

    String uri();

    String pathInfo();

    String parameter(String name);

    <T> T unwrap(Class<T> type);

    String ip();

    Object attribute(Object key);

    void attribute(Object key, Object value);

    void removeAttribute(Object key);

    Session session() throws LeaseException;
}
