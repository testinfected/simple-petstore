package org.testinfected.support;

public interface Session {

    boolean contains(Object key);

    void put(Object key, Object value);

    Object get(Object key);
}
