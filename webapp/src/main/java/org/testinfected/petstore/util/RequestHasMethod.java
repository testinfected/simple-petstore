package org.testinfected.petstore.util;

import org.simpleframework.http.Request;

public class RequestHasMethod implements Matcher<Request> {

    private final Matcher<? super String> method;

    public RequestHasMethod(Matcher<? super String> method) {
        this.method = method;
    }

    public boolean matches(Request actual) {
        return method.matches(actual.getMethod());
    }

    public static RequestHasMethod hasMethod(Matcher<? super String> method) {
        return new RequestHasMethod(method);
    }
}
