package test.support.org.testinfected.molecule.unit;

import org.testinfected.molecule.Session;
import org.testinfected.molecule.util.Clock;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MockSession implements Session {

    private final Map<Object, Object> attributes = new HashMap<Object, Object>();
    private final String id;

    public MockSession() {
        this("mock session");
    }

    public MockSession(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public boolean contains(Object key) {
        return attributes.containsKey(key);
    }

    public Object get(Object key) {
        return attributes.get(key);
    }

    public void put(Object key, Object value) {
        attributes.put(key, value);
    }

    public Set<?> keys() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Collection<?> values() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public long timeout() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void timeout(long inSeconds) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean expired(Clock clock) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void touch(Clock clock) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void invalidate() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean invalid() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Date createdAt() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Date lastAccessedAt() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String toString() {
        return id() + ": " + attributes.toString();
    }
}
