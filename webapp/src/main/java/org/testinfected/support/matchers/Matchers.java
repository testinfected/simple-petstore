package org.testinfected.support.matchers;

import org.simpleframework.http.Path;
import org.testinfected.support.Matcher;
import org.testinfected.support.Request;

public final class Matchers {

    public static <T> Matcher<T> equalTo(T other) {
        return IsEqual.equalTo(other);
    }

    public static <T> Matcher<T> anything() {
        return Anything.anything();
    }

    public static <T> Matcher<T> nothing() {
        return Nothing.nothing();
    }

    public static Matcher<org.simpleframework.http.Request> anyRequest() {
        return anything();
    }

    public static Matcher<String> anyMethod() {
        return anything();
    }

    public static Matcher<org.simpleframework.http.Request> hasPath(Matcher<? super Path> path) {
        return RequestHasPath.hasPath(path);
    }

    public static Matcher<org.simpleframework.http.Request> withPath(Matcher<? super String> path) {
        return RequestHasPath.hasNormalizedPath(path);
    }

    public static Matcher<Request> hasNormalizedPath(Matcher<? super String> path) {
        return RequestWithPath.withPath(path);
    }

    public static Matcher<String> startingWith(String prefix) {
        return StartingWith.startingWith(prefix);
    }

    public static Matcher<org.simpleframework.http.Request> hasMethod(Matcher<? super String> method) {
        return RequestHasMethod.hasMethod(method);
    }

    public static Matcher<org.simpleframework.http.Request> hasMethod(String method) {
        return RequestHasMethod.hasMethod(method);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> allOf(Matcher<T>... matchers) {
        return AllOf.allOf(matchers);
    }

    private Matchers() {}
}
