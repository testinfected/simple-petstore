package com.vtence.molecule.matchers;

import com.vtence.molecule.util.Matcher;

import java.util.Arrays;

public class AllOf<T> implements Matcher<T> {

    private final Iterable<Matcher<? super T>> matchers;

    public AllOf(Iterable<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(T actual) {
        for (Matcher<? super T> matcher : matchers) {
            if (!matcher.matches(actual)) return false;
        }

        return true;
    }

    public static <T> Matcher<T> allOf(Matcher<? super T>... matchers) {
        return new AllOf<T>(Arrays.asList(matchers));
    }
}
