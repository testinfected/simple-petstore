package com.vtence.molecule.lib;

import org.junit.Test;

import static com.vtence.molecule.lib.StartingWith.startingWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AllOfTest {

    @SuppressWarnings("unchecked") @Test public void
    evaluatesToLogicalConjunctionOfMultipleMatchers() {
        Matcher<String> allOf = AllOf.allOf(startingWith("one"),
                                            startingWith("one two"), startingWith("one two three"));

        assertThat("all match", allOf.matches("one two three"), is(true));
        assertThat("one miss", !allOf.matches("one two two"), is(true));
    }

    @SuppressWarnings("unchecked") @Test public void
    matchesDescendantType() {
        Matcher<String> allOf = AllOf.allOf(startingWith("good"), aMatcherOnType(Object.class));

        assertThat("matches", allOf.matches("good"), is(true));
    }

    private <T> Anything<T> aMatcherOnType(Class<T> _) {
        return new Anything<T>();
    }
}
