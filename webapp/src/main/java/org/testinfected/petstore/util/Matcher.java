package org.testinfected.petstore.util;

public interface Matcher<T> {

    boolean matches(T actual);
}
