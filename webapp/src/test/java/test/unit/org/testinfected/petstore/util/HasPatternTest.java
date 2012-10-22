package test.unit.org.testinfected.petstore.util;

import org.junit.Test;
import org.testinfected.petstore.util.HasPattern;
import org.testinfected.petstore.util.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

public class HasPatternTest {

    Matcher<? super String> matcher = HasPattern.pattern("/path/([^/]+)/resource");

    @Test public void
    matchesWhenTextMatchesPattern() {
        assertThat("no match", matcher.matches("/path/to/resource"));
    }

    @Test public void
    doesNotMatchWhenTextDoesNotMatchPattern() {
        assertThat("match", !matcher.matches("/path"));
        assertThat("match", !matcher.matches("/path///resource"));
        assertThat("match", !matcher.matches("/path/to/elsewhere"));
    }
}
