package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Request;
import org.testinfected.petstore.util.RequestMatcher;

public class Nothing implements RequestMatcher {
    public boolean matches(Request request) {
        return false;
    }

    public static Nothing nothing() {
        return new Nothing();
    }
}
