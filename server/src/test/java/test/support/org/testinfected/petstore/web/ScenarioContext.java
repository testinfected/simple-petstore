package test.support.org.testinfected.petstore.web;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private final Map<Object, Object> values = new HashMap<>();

    public <T> void set(T value) {
        set(value.getClass(), value);
    }

    public <T> void set(Object key, T value) {
        values.put(key, value);
    }

    public <T> T get(Class<T> key) {
        return get((Object) key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        if (!values.containsKey(key))
            throw new IllegalArgumentException(String.valueOf(key));

        return (T) values.get(key);
    }
}