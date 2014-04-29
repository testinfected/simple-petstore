package com.vtence.molecule.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Headers {

    private final Map<String, List<String>> values = new HashMap<String, List<String>>();
    private final Map<String, String> names = new LinkedHashMap<String, String>();

    public String get(String name) {
        return has(name) ? Joiner.on(", ").join(values(name)) : null;
    }

    public List<String> list(String name) {
        return new ArrayList<String>(values(name));
    }

    public boolean has(String name) {
        return values.containsKey(name) || values.containsKey(canonicalForm(name));
    }

    public int size() {
        return values.size();
    }

    public List<String> names() {
        return new ArrayList<String>(new LinkedHashSet<String>(names.values()));
    }

    public Map<String, String> all() {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        for (String name : names()) {
            headers.put(name, get(name));
        }
        return headers;
    }

    public void put(String name, String value) {
        List<String> all = values(name);
        all.clear();
        add(name, value);
    }

    public void add(String name, String value) {
        values(name).add(value);
        if (literalForm(name) != null && !literalForm(name).equals(name)) {
            names.remove(literalForm(name));
        }
        names.put(canonicalForm(name), name);
        names.put(name, name);
    }

    public void remove(String name) {
        names.remove(literalForm(name));
        names.remove(canonicalForm(name));
        values.remove(canonicalForm(name));
    }

    private String literalForm(String name) {
        return names.get(canonicalForm(name));
    }

    private String canonicalForm(String name) {
        return name.toLowerCase();
    }

    private List<String> values(String name) {
        if (!values.containsKey(canonicalForm(name))) {
            values.put(canonicalForm(name), new ArrayList<String>());
        }
        return values.get(canonicalForm(name));
    }
}
