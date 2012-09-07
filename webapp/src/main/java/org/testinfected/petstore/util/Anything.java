package org.testinfected.petstore.util;

import org.simpleframework.http.Request;

public class Anything implements RequestMatcher {
    public boolean matches(Request request) {
        return true;
    }

    public static Anything anything() {
        return new Anything();
    }
}
