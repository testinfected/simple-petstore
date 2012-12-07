package org.testinfected.support.routing;

import org.simpleframework.http.Path;
import org.simpleframework.http.parse.PathParser;
import org.testinfected.support.util.Matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicPath implements Matcher<String> {

    private final String pattern;

    public DynamicPath(String pattern) {
        this.pattern = pattern;
    }

    public boolean matches(String actual) {
        return sameLength(segments(pattern), segments(actual)) && match(segments(pattern), segments(actual));
    }

    private boolean match(String[] expectedSegments, String[] actualSegments) {
        for (int i = 0; i < expectedSegments.length; i++) {
            String expected = expectedSegments[i];
            String actual = actualSegments[i];
            if (!(expected.equals(actual) || isDynamic(expected))) return false;
        }
        return true;
    }

    private boolean sameLength(String[] expected, String[] actual) {
        return expected.length == actual.length;
    }

    private String[] segments(String path) {
        return removeEmptyParts(path.split("/"));
    }

    private String[] removeEmptyParts(String[] parts) {
        List<String> significantParts = new ArrayList<String>();
        for (String part : parts) {
            if (!part.isEmpty()) significantParts.add(part);
        }
        return significantParts.toArray(new String[significantParts.size()]);
    }

    private boolean isDynamic(String segment) {
        return segment.startsWith(":");
    }

    public Map<String, String> boundParameters(String path) {
        Path actual = new PathParser(path);
        Map<String, String> boundParameters = new HashMap<String, String>();

        String[] parts = segments(this.pattern);
        for (int i = 0; i < parts.length; i++) {
            String segment = parts[i];
            if (isDynamic(segment)) boundParameters.put(stripLeadingColonFrom(segment), actual.getSegments()[i]);
        }
        return boundParameters;
    }

    private String stripLeadingColonFrom(String segment) {
        return segment.substring(1);
    }
}
