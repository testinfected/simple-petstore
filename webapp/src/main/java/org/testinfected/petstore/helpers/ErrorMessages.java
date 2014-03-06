package org.testinfected.petstore.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErrorMessages {

    private final Map<String, Set<String>> errors = new HashMap<String, Set<String>>();

    public void add(String path, String message) {
        if (!errors.containsKey(path)) errors.put(path, new HashSet<String>());
        errors.get(path).add(message);
    }

    public boolean contains(String key) {
        return errors.containsKey(key);
    }

    public Map<String, Set<String>> all() {
        return new HashMap<String, Set<String>>(errors);
    }

    public Iterable<String> at(String path) {
        if (!errors.containsKey(path)) errors.put(path, new HashSet<String>());
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
