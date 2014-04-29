package com.vtence.molecule.support;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.helpers.Streams;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.both;

public class HttpResponse {

    private final WebResponse response;

    public HttpResponse(WebResponse response) {
        this.response = response;
    }

    public void assertOK() {
        assertHasStatusCode(200);
    }

    public void assertHasStatusCode(int code) {
        assertThat("response", response, HasStatusCode.hasStatusCode(code));
    }

    public void assertHasStatusMessage(String message) {
        assertThat("response", response, HasStatusMessage.hasStatusMessage(message));
    }

    public void assertHasStatus(HttpStatus status) {
        assertHasStatusCode(status.code);
        assertHasStatusMessage(status.text);
    }

    public void assertHasNoHeader(String name) {
        assertThat("response", response, HasHeaderWithValue.hasNoHeader(name));
    }

    public void assertHasHeader(String name, Matcher<? super String> valueMatcher) {
        assertThat("response", response, HasHeaderWithValue.hasHeader(name, valueMatcher));
    }

    public void assertHasHeader(String name, String value) {
        assertThat("response", response, HasHeaderWithValue.hasHeader(name, value));
    }

    public void assertHasContent(byte[] content) throws IOException, URISyntaxException {
        Assert.assertArrayEquals("content", content, content());
    }

    public void assertHasContent(String content) {
        assertHasContent(equalTo(content));
    }

    public void assertHasContent(Matcher<? super String> contentMatcher) {
        assertThat("response", response, HasContent.hasContent(contentMatcher));
    }

    public void assertContentIsEncodedAs(String charset) throws IOException {
        assertThat("response encoding", CharsetDetector.detectedCharset(content()).toLowerCase(), containsString(charset.toLowerCase()));
    }

    public void assertHasContentSize(long length) throws IOException {
        assertHasContentSize((int) length);
    }

    public void assertHasContentSize(int size) throws IOException {
        assertThat("response size", content().length, equalTo(size));
    }

    public void assertHasContentType(String contentType) {
        assertHasHeader("Content-Type", equalTo(contentType));
    }

    public void assertHasContentType(Matcher<? super String> contentTypeMatcher) {
        assertHasHeader("Content-Type", contentTypeMatcher);
    }

    private byte[] content() throws IOException {
        return Streams.toBytes(response.getContentAsStream());
    }

    public void assertChunked() {
        assertHasHeader("Transfer-Encoding", "chunked");
    }

    public void assertNotChunked() {
        assertHasNoHeader("Transfer-Encoding");
    }

    @SuppressWarnings("unchecked")
    public void assertHasCookie(String name) {
        assertHasCookie(name, any(String.class));
    }

    public void assertHasNoCookie(String name) {
        assertHasCookie(not(startsWith(name)));
    }

    public void assertHasCookie(Matcher<? super String> matching) {
        assertHasHeader("Set-Cookie", matching);
    }

    public void assertHasCookie(String name, Matcher<? super String> matching) {
        assertHasCookie(both(startsWith(name + "=")).and(matching));
    }
}
