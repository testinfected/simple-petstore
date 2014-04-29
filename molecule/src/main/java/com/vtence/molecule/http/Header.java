package com.vtence.molecule.http;

import com.vtence.molecule.helpers.Joiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Header {

    private static final String ZERO_OR_EVEN_NUMBER_OF_QUOTES_AHEAD = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private static Pattern ignoringQuotedStrings(String delimiter) {
        return Pattern.compile("\\s*" + delimiter + "\\s*" + ZERO_OR_EVEN_NUMBER_OF_QUOTES_AHEAD);
    }

    private static final Pattern VALUES_DELIMITER = ignoringQuotedStrings(",");
    private static final Pattern TOKENS_DELIMITER = ignoringQuotedStrings(";");
    private static final Pattern NAME_VALUE_DELIMITER = ignoringQuotedStrings("=");

    private final List<Value> values;

    public Header(String header) {
        this.values = sortByQuality(parseValues(header));
    }

    public List<Value> all() {
        return new ArrayList<Value>(values);
    }

    public Value first() {
        return values.get(0);
    }

    public List<String> values() {
        List<String> values = new ArrayList<String>();
        for (Value entry : this.values) {
            if (entry.acceptable()) values.add(entry.value());
        }
        return values;
    }

    private List<Value> parseValues(String header) {
        List<Value> values = new ArrayList<Value>();
        for (String value : VALUES_DELIMITER.split(header)) {
            String[] tokens = TOKENS_DELIMITER.split(value);
            values.add(new Value(value(tokens), parameters(tokens)));
        }
        return values;
    }

    private String value(String[] tokens) {
        return tokens[0];
    }

    private List<Parameter> parameters(String[] tokens) {
        List<Parameter> pairs = new ArrayList<Parameter>();
        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i];
            String[] parts = NAME_VALUE_DELIMITER.split(token);
            String attribute = parts[0];
            String value = parts.length > 1 ? parts[1] : null;
            pairs.add(new Parameter(attribute, value));
        }

        return pairs;
    }

    public String toString() {
        return Joiner.on(", ").join(values);
    }

    private List<Value> sortByQuality(List<Value> entries) {
        Collections.sort(entries);
        return entries;
    }

    public static class Value implements Comparable<Value> {
        private final String value;
        private final double quality;
        private final List<Parameter> parameters;

        public Value(String value, List<Parameter> parameters) {
            this.value = value.trim();
            this.parameters = parameters;
            this.quality = parseQuality();
        }

        public String value() {
            return value;
        }

        public boolean is(String value) {
            return this.value.equals(value);
        }

        public double quality() {
            return quality;
        }

        public boolean acceptable() {
            return quality > 0;
        }

        public List<Parameter> parameters() {
            return parameters;
        }

        public String parameter(String name) {
            for (Parameter parameter : parameters()) {
                if (parameter.is(name)) return parameter.value();
            }
            return null;
        }

        private double parseQuality() {
            if (parameters.isEmpty()) return 1.0;

            Parameter first = parameters.get(0);
            if (!first.is("q") || first.value() == null) return 1.0;

            try {
                return Double.parseDouble(first.value());
            } catch (NumberFormatException e) {
                return 1.0;
            }
        }

        public int compareTo(Value other) {
            return Double.compare(other.quality(), quality);
        }

        public String toString() {
            if (parameters.isEmpty()) return value;
            return value + "; " + Joiner.on("; ").join(parameters);
        }
    }

    public static class Parameter {
        private final String name;
        private final String value;

        public Parameter(String name, String value) {
            this.name = name.trim();
            this.value = value != null ? value.trim() : null;
        }

        public String name() {
            return name;
        }

        public boolean is(String name) {
            return this.name().equals(name);
        }

        public String value() {
            return value;
        }

        public String toString() {
            return name + (value != null ? "=" + value : "");
        }
    }
}
