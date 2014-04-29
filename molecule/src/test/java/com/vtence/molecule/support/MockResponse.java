package com.vtence.molecule.support;

import com.vtence.molecule.http.Cookie;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Response;
import org.hamcrest.Matcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.vtence.molecule.http.HeaderNames.LOCATION;
import static com.vtence.molecule.support.CharsetDetector.detectedCharset;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class MockResponse extends Response {

    public void assertStatusCode(int code) {
        assertThat("status code", statusCode(), equalTo(code));
    }

    public void assertStatusText(String text) {
        assertThat("status text", statusText(), equalTo(text));
    }

    public void assertStatus(HttpStatus expected) {
        assertStatusCode(expected.code);
        assertStatusText(expected.text);
    }

    public void assertRedirectedTo(String location) {
        assertThat("redirection", get(LOCATION), equalTo(location));
    }

    public void assertHeader(String name, String value) {
        assertHeader(name, equalTo(value));
    }

    public void assertHeader(String name, Matcher<? super String> matching) {
        assertThat(name, get(name), matching);
    }

    public void assertNoHeader(String name) {
        assertHeader(name, nullValue());
    }

    public void assertContentType(String contentType) {
        assertContentType(equalTo(contentType));
    }

    public void assertContentType(Matcher<? super String> matching) {
        assertHeader("Content-Type", matching);
    }

    public void assertHasCookie(String name) {
        assertCookie(name, notNullValue());
    }

    public void assertHasNoCookie(String name) {
        assertCookie(name, nullValue());
    }

    public void assertCookie(String name, Matcher<? super Cookie> matching) {
        assertThat(name, cookie(name), matching);
    }

    public void assertBody(String body) {
        assertBody(equalTo(body));
    }

    public void assertBody(Matcher<? super String> matching) {
        assertThat("body", text(), matching);
    }

    public void assertContent(byte[] content) {
        assertArrayEquals("content", content, content());
    }

    public void assertContentSize(long size) {
        assertThat("content size", contentSize(), is(size));
    }

    public void assertContentEncodedAs(String encoding) throws IOException {
        assertThat("content encoding", detectedCharset(content()).toLowerCase(), containsString(encoding.toLowerCase()));
    }

    public byte[] content() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            body().writeTo(out, charset());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return out.toByteArray();
    }

    public InputStream stream() {
        return new ByteArrayInputStream(content());
    }

    public String text() {
        return new String(content(), charset());
    }

    public long contentSize() {
        return content().length;
    }

    public String toString() {
        // todo add headers and status as well
        return text();
    }
}