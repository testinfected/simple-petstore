package org.testinfected.molecule.matchers;

import org.testinfected.molecule.util.Matcher;

public class Nothing<T> implements Matcher<T> {
    public boolean matches(T actual) {
        return false;
    }

    public static <T> Nothing<T> nothing() {
        return new Nothing<T>();
    }
}
