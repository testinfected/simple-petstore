package test.unit.org.testinfected.petstore.util;

import org.junit.Test;
import org.testinfected.petstore.util.Matcher;
import org.testinfected.petstore.util.StartingWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StartingWithTest {

    Matcher<String> matcher = StartingWith.startingWith("excerpt");

    @Test public void
    matchesStringStartingWithExcerpt() {
        assertThat("match", matcher.matches("excerpt ..."), equalTo(true));
    }

    @Test public void
    doesNotMatchStringEndingWithExcerpt() {
        assertThat("match", matcher.matches("... excerpt"), equalTo(false));
    }

    @Test public void
    matchesSameString() {
        assertThat("match", matcher.matches("excerpt"), equalTo(true));
    }
}

