package org.testinfected.petstore.util;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<String, Object> map = new HashMap<String, Object>();

    public static Context context() {
        return new Context();
    }

    public static Context empty() {
        return context();
    }

    public Context with(String name, Object value) {
        map.put(name, value);
        return this;
    }

    public Context and(String name, Object value) {
        return with(name, value);
    }

    public Map<String, Object> asMap() {
        return map;
    }
}
