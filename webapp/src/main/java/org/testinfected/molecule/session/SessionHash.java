package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHash implements Session {

    private final String id;
    private final Map<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();

    public SessionHash(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public boolean contains(Object key) {
        return attributes.containsKey(key);
    }

    public void put(Object key, Object value) {
        attributes.put(key, value);
    }

    public Object get(Object key) {
        return attributes.get(key);
    }

    public Set<Object> keys() {
        return Collections.unmodifiableSet(attributes.keySet());
    }

    public Collection<Object> values() {
        return Collections.unmodifiableCollection(attributes.values());
    }

    public String toString() {
        return id + ": " + attributes.toString();
    }
}