package com.vtence.molecule.http;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class HeaderTest {

    @SuppressWarnings("unchecked") @Test public void
    parsesAndSortsValuesInQualityOrder() {
        Header header = new Header("foo; q=0.5, bar, baz; q=0.9, qux, *; q=0");
        assertThat("header", header.toString(),
                equalTo("bar, qux, baz; q=0.9, foo; q=0.5, *; q=0"));
    }

    @Test public void
    parsesParametersAsAttributeValuePairs() {
        Header header = new Header("foo; q=0.5; bar; baz= ; qux");
        assertThat("header", header.toString(),
                equalTo("foo; q=0.5; bar; baz; qux"));
    }

    @SuppressWarnings("unchecked") @Test public void
    ignoresLeadingAndTrailingWhitespace() {
        Header header = new Header("  foo,   bar ;   q  =  0.9   ");
        assertThat("header", header.toString(), equalTo("foo, bar; q=0.9"));
    }

    @SuppressWarnings("unchecked") @Test public void
    recognizesQuotedStringsInValues() {
        Header header = new Header("\"foo, bar\"; q=0.8, baz, \"qux; q=0.6\"; q=0.6");
        assertThat("header", header.toString(),
                equalTo("baz, \"foo, bar\"; q=0.8, \"qux; q=0.6\"; q=0.6"));
    }

    @SuppressWarnings("unchecked") @Test public void
    recognizesQuotedStringsInParameters() {
        Header header = new Header("foo; q=0.8, bar; \"q=0.5\"; \", baz; q=0.8\"");
        assertThat("header", header.toString(),
                equalTo("bar; \"q=0.5\"; \", baz; q=0.8\", foo; q=0.8"));
    }

    @SuppressWarnings("unchecked") @Test public void
    listAcceptableValues() {
        Header header = new Header("foo, bar; q=0.8, baz, qux; q=0");
        assertThat("acceptable values", header.values(), contains("foo", "baz", "bar"));
    }

    @SuppressWarnings("unchecked") @Test public void
    ignoresQualityIfNotFirstParameterOrNotANumber() {
        Header header = new Header("foo; bar; q=0, baz; q=0.8, qux; q=_");
        assertThat("acceptable values", header.values(), contains("foo", "qux", "baz"));
    }
}
