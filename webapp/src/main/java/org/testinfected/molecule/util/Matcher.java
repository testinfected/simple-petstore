package org.testinfected.molecule.util;

public interface Matcher<T> {

    boolean matches(T actual);
}
