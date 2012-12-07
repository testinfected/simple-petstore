package org.testinfected.support.util;

public interface Matcher<T> {

    boolean matches(T actual);
}
