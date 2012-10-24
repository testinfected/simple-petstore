package org.testinfected.petstore.routing;

import org.simpleframework.http.Path;
import org.simpleframework.http.parse.PathParser;
import org.testinfected.petstore.util.Matcher;

import java.util.HashMap;
import java.util.Map;

public class DynamicPath implements Matcher<Path> {

    private final Path pattern;

    public DynamicPath(String path) {
        this.pattern = new PathParser(path);
    }

    public boolean matches(Path actual) {
        return matchesNumberOfSegments(actual) && matchesSegments(actual);
    }

    private boolean matchesNumberOfSegments(Path actual) {
        return pattern.getSegments().length == actual.getSegments().length;
    }

    private boolean matchesSegments(Path actual) {
        String[] parts = pattern.getSegments();
        String[] actualSegments = actual.getSegments();

        for (int i = 0; i < parts.length; i++) {
            String segment = parts[i];
            if (!isDynamic(segment) && !segment.equals(actualSegments[i])) return false;
        }
        return true;
    }

    private boolean isDynamic(String segment) {
        return segment.startsWith(":");
    }

    public Map<String, String> extractBoundParameters(Path actual) {
        Map<String, String> boundParameters = new HashMap<String, String>();

        String[] parts = pattern.getSegments();
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
