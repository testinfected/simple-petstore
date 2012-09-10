package org.testinfected.petstore.util;

public class Anything<T> implements Matcher<T> {
    public boolean matches(T actual) {
        return true;
    }
}
