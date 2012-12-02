package org.testinfected.support.matchers;

import org.testinfected.support.Matcher;

public class Nothing<T> implements Matcher<T> {
    public boolean matches(T actual) {
        return false;
    }
}
