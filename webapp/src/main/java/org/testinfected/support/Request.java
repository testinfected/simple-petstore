package org.testinfected.support;

public interface Request {

    String protocol();

    String method();

    String uri();

    String getParameter(String name);

    <T> T unwrap(Class<T> type);

    String ip();
}
