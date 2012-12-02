package org.testinfected.support.matchers;

import org.testinfected.support.Matcher;

import java.util.Arrays;
import java.util.List;

public class Combination<T> implements Matcher<T> {

    private final Matcher<? super T> matcher;

    public Combination(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    public boolean matches(T actual) {
        return matcher.matches(actual);
    }

    public static <LHS> Combination<LHS> both(Matcher<? super LHS> matcher) {
        return new Combination<LHS>(matcher);
    }

    public Combination<T> and(Matcher<? super T> other) {
        return new Combination<T>(new AllOf<T>(asList(other)));
    }

    @SuppressWarnings("unchecked")
    private List<Matcher<? super T>> asList(Matcher<? super T> other) {
        return Arrays.<Matcher<? super T>>asList(matcher, other);
    }
}
