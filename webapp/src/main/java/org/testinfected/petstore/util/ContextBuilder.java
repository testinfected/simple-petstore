package org.testinfected.petstore.util;

import java.util.HashMap;
import java.util.Map;

public class ContextBuilder {

    private final Map<String, Object> map = new HashMap<String, Object>();

    public static ContextBuilder context() {
        return new ContextBuilder();
    }

    public static Map<String, Object> emptyContext() {
        return context().asMap();
    }

    public ContextBuilder with(String name, Object attribute) {
        map.put(name, attribute);
        return this;
    }

    public ContextBuilder and(String name, Object attribute) {
        return with(name, attribute);
    }

    public Map<String, Object> asMap() {
        return map;
    }
}
