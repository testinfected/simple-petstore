package com.vtence.molecule.lib;

import java.util.Arrays;

public class AnyOf<T> implements Matcher<T> {

    private final Iterable<Matcher<? super T>> matchers;

    public AnyOf(Iterable<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(T actual) {
        for (Matcher<? super T> matcher : matchers) {
            if (matcher.matches(actual)) return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> anyOf(Matcher<? super T>... matchers) {
        return new AnyOf<T>(Arrays.asList(matchers));
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> anyOf(Iterable<Matcher<? super T>> matchers) {
        return new AnyOf<T>(matchers);
    }
}
