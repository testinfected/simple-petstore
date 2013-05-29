package test.support.org.testinfected.molecule.unit;

import org.testinfected.molecule.Session;

import java.util.HashMap;
import java.util.Map;

public class MockSession implements Session {

    private final Map<Object, Object> attributes = new HashMap<Object, Object>();

    public boolean contains(Object key) {
        return attributes.containsKey(key);
    }

    public Object get(Object key) {
        return attributes.get(key);
    }

    public void put(Object key, Object value) {
        attributes.put(key, value);
    }
}
