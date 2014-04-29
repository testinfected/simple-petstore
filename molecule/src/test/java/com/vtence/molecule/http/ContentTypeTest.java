package com.vtence.molecule.http;

import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class ContentTypeTest {

    @Test public void
    parsesMediaType() {
        ContentType contentType = ContentType.parse("text/plain");
        assertThat("media type", contentType.mediaType(), equalTo("text/plain"));
    }

    @Test public void
    parsesTypeAndSubType() {
        ContentType contentType = ContentType.parse("text/html");
        assertThat("type", contentType.type(), equalTo("text"));
        assertThat("subtype", contentType.subType(), equalTo("html"));
    }

    @Test public void
    handlesAbsenceOfASubType() {
        ContentType contentType = ContentType.parse("text");
        assertThat("type", contentType.type(), equalTo("text"));
        assertThat("subtype", contentType.subType(), nullValue());
    }

    @Test public void
    parsesCharset() {
        ContentType contentType = ContentType.parse("text/html; charset=utf-8");
        assertThat("charset name", contentType.charsetName(), equalTo("utf-8"));
        assertThat("charset", contentType.charset().name(), equalTo("UTF-8"));
    }

    @Test public void
    hasAStringRepresentation() {
        ContentType contentType = ContentType.parse("text/html; charset=utf-8");
        assertThat("header", contentType.toString(), equalTo("text/html; charset=utf-8"));
    }

    @Test public void
    handlesAbsenceOfACharset() {
        ContentType contentType = ContentType.parse("text/html");
        assertThat("charset", contentType.charset(), nullValue());
        assertThat("header", contentType.toString(), equalTo("text/html"));
    }

    @Test public void
    handlesAbsenceOfHeaderInResponse() {
        assertThat("content type", ContentType.of(new MockResponse()), nullValue());
    }
}
