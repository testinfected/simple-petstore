package org.testinfected.molecule.matchers;

import org.testinfected.molecule.util.Matcher;

public class IsEqual<T> implements Matcher<T> {
    private final T other;

    public IsEqual(T other) {
        this.other = other;
    }

    public boolean matches(T actual) {
        return other.equals(actual);
    }

    public static <T> IsEqual<T> equalTo(T other) {
        return new IsEqual<T>(other);
    }
}
