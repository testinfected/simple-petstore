package org.testinfected.support;

public interface Matcher<T> {

    boolean matches(T actual);
}
