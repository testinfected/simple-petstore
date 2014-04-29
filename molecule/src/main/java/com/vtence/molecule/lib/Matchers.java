package com.vtence.molecule.lib;

import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.Request;

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

    public static Matcher<Request> withPath(Matcher<? super String> path) {
        return RequestWithPath.withPath(path);
    }

    public static Matcher<String> startingWith(String prefix) {
        return StartingWith.startingWith(prefix);
    }

    public static Matcher<Request> withMethod(String name) {
        return RequestWithMethod.withMethod(name);
    }

    public static Matcher<Request> withMethod(HttpMethod method) {
        return RequestWithMethod.withMethod(method);
    }

    public static Matcher<Request> withMethod(Matcher<? super HttpMethod> method) {
        return RequestWithMethod.withMethod(method);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> allOf(Matcher<? super T>... matchers) {
        return AllOf.allOf(matchers);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> anyOf(Iterable<Matcher<? super T>> matchers) {
        return AnyOf.anyOf(matchers);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> anyOf(Matcher<? super T>... matchers) {
        return AnyOf.anyOf(matchers);
    }

    private Matchers() {}
}
