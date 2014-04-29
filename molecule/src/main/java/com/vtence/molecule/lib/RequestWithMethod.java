package com.vtence.molecule.lib;

import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.Request;

public class RequestWithMethod implements Matcher<Request> {

    private final Matcher<? super HttpMethod> methodMatcher;

    public RequestWithMethod(Matcher<? super HttpMethod> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public boolean matches(Request actual) {
        return methodMatcher.matches(actual.method());
    }

    public static RequestWithMethod withMethod(String name) {
        return withMethod(HttpMethod.valueOf(name));
    }

    public static RequestWithMethod withMethod(HttpMethod method) {
        return withMethod(Matchers.equalTo(method));
    }

    public static RequestWithMethod withMethod(Matcher<? super HttpMethod> method) {
        return new RequestWithMethod(method);
    }
}
