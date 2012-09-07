package org.testinfected.petstore.util;

public final class Matchers {

    public static RequestMatcher anything() {
        return Anything.anything();
    }

    private Matchers() {}
}
