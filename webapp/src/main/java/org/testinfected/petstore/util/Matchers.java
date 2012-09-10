package org.testinfected.petstore.util;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;

public final class Matchers {

    public static <T> Matcher<T> equalTo(T other) {
        return new IsEqual<T>(other);
    }

    public static Matcher<Request> anyRequest() {
        return new Anything<Request>();
    }

    public static Matcher<String> anyPath() {
        return new Anything<String>();
    }

    public static Matcher<String> anyMethod() {
        return new Anything<String>();
    }

    public static Matcher<Request> hasPath(Matcher<Path> path) {
        return RequestHasPath.hasPath(path);
    }

    public static Matcher<Request> hasNormalizedPath(Matcher<String> path) {
        return RequestHasPath.hasNormalizedPath(path);
    }

    public static Matcher<String> startingWith(String prefix) {
        return StartingWith.startingWith(prefix);
    }

    public static Matcher<Request> hasMethod(Matcher<String> method) {
        return RequestHasMethod.hasMethod(method);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> allOf(Matcher<T>... matchers) {
        return AllOf.allOf(matchers);
    }

    private Matchers() {}
}
