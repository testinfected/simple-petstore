package org.testinfected.support.matchers;

import org.testinfected.support.Matcher;
import org.testinfected.support.Request;

import static org.testinfected.support.matchers.IsEqual.equalTo;

public class RequestWithMethod implements Matcher<Request> {

    private final Matcher<? super String> method;

    public RequestWithMethod(Matcher<? super String> method) {
        this.method = method;
    }

    public boolean matches(Request actual) {
        return method.matches(actual.method());
    }

    public static RequestWithMethod withMethod(String method) {
        return withMethod(equalTo(method));
    }

    public static RequestWithMethod withMethod(Matcher<? super String> method) {
        return new RequestWithMethod(method);
    }
}
