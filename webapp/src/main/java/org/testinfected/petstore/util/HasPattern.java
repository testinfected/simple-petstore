package org.testinfected.petstore.util;

public class HasPattern implements Matcher<String> {

    private final String pattern;

    public HasPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean matches(String actual) {
        return actual.matches(pattern);
    }

    public static Matcher<? super String> pattern(String pattern) {
        return new HasPattern(pattern);
    }
}
