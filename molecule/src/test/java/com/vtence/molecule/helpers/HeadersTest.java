package com.vtence.molecule.helpers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class HeadersTest {

    Headers headers = new Headers();

    @Test public void
    retainsHeaderNamesCase() {
        headers.put("Content-Type", "text/plain");
        headers.put("transfer-encoding", "chunked");
        headers.put("ALLOW", "GET");

        assertThat("names", headers.names(), contains("Content-Type", "transfer-encoding", "ALLOW"));

        assertThat("headers", headers.all(), allOf(
                hasEntry("Content-Type", "text/plain"),
                hasEntry("transfer-encoding", "chunked"),
                hasEntry("ALLOW", "GET")
        ));
    }

    @Test public void
    accessCaseInsensitively() {
        headers.put("header", "header");
        assertThat("Header", headers.get("Header"), equalTo("header"));
    }

    @Test public void
    checksExistenceOfKeysCaseInsensitively() {
        headers.put("Header", "header");
        assertThat("has Header", headers.has("Header"), is(true));
        assertThat("has header", headers.has("header"), is(true));
        assertThat("has HEADER", headers.has("HEADER"), is(true));
    }

    @Test public void
    overwritesNamesCaseInsensitivelyAndAssumeTheNewCase() {
        headers.put("HEADER-NAME", "oldest value");
        headers.put("header-name", "older value");
        headers.put("Header-Name", "new value");

        assertThat("header names", headers.names(), contains("Header-Name"));
        assertThat("headers", headers.size(), equalTo(1));
        assertThat("headers", headers.all(), hasEntry("Header-Name", "new value"));
    }

    @Test public void
    deletesNamesCaseInsensitively() {
        headers.put("removed", "still there");

        headers.remove("Removed");
        assertThat("names", headers.names(), emptyIterable());
        assertThat("still there", headers.has("removed"), is(false));
        assertThat("removed", headers.get("removed"), nullValue());
    }

    @Test public void
    maintainsAListOfValuesForEachName() {
        headers.add("allow", "GET");
        headers.add("ALLOW", "HEAD");
        headers.add("Allow", "OPTIONS");

        assertThat("names", headers.names(), contains("Allow"));
        assertThat("Allow", headers.list("Allow"), contains("GET", "HEAD", "OPTIONS"));
        assertThat("Allow", headers.get("Allow"), equalTo("GET, HEAD, OPTIONS"));
    }
}
