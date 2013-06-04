package test.support.org.testinfected.molecule.unit;

import org.testinfected.molecule.Session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MockSession implements Session {

    private final Map<Object, Object> attributes = new HashMap<Object, Object>();

    public String id() {
        throw new UnsupportedOperationException("Not implemented");
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
}
