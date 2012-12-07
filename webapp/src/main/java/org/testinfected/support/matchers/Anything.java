package org.testinfected.support.matchers;

import org.testinfected.support.util.Matcher;

public class Anything<T> implements Matcher<T> {
    public boolean matches(T actual) {
        return true;
    }

    public static <T> Anything<T> anything() {
        return new Anything<T>();
    }
}
