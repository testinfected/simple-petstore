package org.testinfected.support.simple;

import org.testinfected.support.Session;

public class SimpleSession implements Session {

    private final org.simpleframework.http.session.Session session;

    public SimpleSession(org.simpleframework.http.session.Session session) {
        this.session = session;
    }

    public boolean contains(Object key) {
        return session.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public void put(Object key, Object value) {
        session.put(key, value);
    }

    public Object get(Object key) {
        return session.get(key);
    }
}
