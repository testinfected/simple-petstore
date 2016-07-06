package org.testinfected.petstore.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErrorMessages {

    private final Map<String, Set<String>> errors = new HashMap<>();

    public void add(String path, String message) {
        at(path).add(message);
    }

    public boolean contains(String key) {
        return errors.containsKey(key);
    }

    public Map<String, Set<String>> all() {
        return new HashMap<>(errors);
    }

    public Set<String> at(String path) {
        if (!errors.containsKey(path)) errors.put(path, new HashSet<>());
        return errors.get(path);
    }

    public boolean empty() {
        return errors.isEmpty();
    }

    public String toString() {
        return errors.toString();
    }

    public void addAll(ErrorMessages other) {
        errors.putAll(other.all());
    }
}
