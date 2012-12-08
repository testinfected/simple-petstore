package org.testinfected.molecule.matchers;

import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.util.Matcher;
import org.testinfected.molecule.Request;

import static org.testinfected.molecule.matchers.IsEqual.equalTo;

public class RequestWithMethod implements Matcher<Request> {

    private final Matcher<? super String> nameMatcher;

    public RequestWithMethod(Matcher<? super String> nameMatcher) {
        this.nameMatcher = nameMatcher;
    }

    public boolean matches(Request actual) {
        return nameMatcher.matches(actual.method().name());
    }

    public static RequestWithMethod withMethod(HttpMethod method) {
        return withMethod(method.name());
    }

    public static RequestWithMethod withMethod(String methodName) {
        return withMethod(equalTo(methodName));
    }

    public static RequestWithMethod withMethod(Matcher<? super String> nameMatcher) {
        return new RequestWithMethod(nameMatcher);
    }
}
