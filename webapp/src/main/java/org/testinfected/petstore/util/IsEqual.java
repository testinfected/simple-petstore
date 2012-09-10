package org.testinfected.petstore.util;

public class IsEqual<T> implements Matcher<T> {
    private final T expected;

    public IsEqual(T expected) {
        this.expected = expected;
    }

    public boolean matches(T actual) {
        return expected.equals(actual);
    }
}
