package com.vtence.molecule.matchers;

import org.junit.Test;
import com.vtence.molecule.util.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static com.vtence.molecule.matchers.AllOf.allOf;
import static com.vtence.molecule.matchers.StartingWith.startingWith;

public class AllOfTest {

    @SuppressWarnings("unchecked")
    @Test public void
    evaluatesToLogicalConjunctionOfMultipleMatchers() {
        Matcher<String> allOf = allOf(startingWith("one"), startingWith("one two"), startingWith("one two three"));

        assertThat("no match but all matchers match", allOf.matches("one two three"));
        assertThat("match but one matcher does not match", !allOf.matches("one two two"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    supportsMatchesOnSuperType() {
        Matcher<String> allOf = allOf(startingWith("good"), new Anything<Object>());

        assertThat("match", allOf.matches("good"));
    }
}
