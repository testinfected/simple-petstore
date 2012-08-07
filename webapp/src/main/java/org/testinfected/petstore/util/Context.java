package org.testinfected.petstore.util;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<String, Object> map = new HashMap<String, Object>();

    public static Context context() {
        return new Context();
    }

    public static Map<String, Object> emptyContext() {
        return context().asMap();
    }

    public Context with(String name, Object attribute) {
        map.put(name, attribute);
        return this;
    }

    public Context and(String name, Object attribute) {
        return with(name, attribute);
    }

    public Map<String, Object> asMap() {
        return map;
    }
}
