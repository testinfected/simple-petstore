package test.support.org.testinfected.molecule.web;

import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MockResponse implements Response {

    private final Map<String, String> headers = new HashMap<String, String>();
    private String contentType;
    private HttpStatus status;
    private String location;

    public static MockResponse aResponse() {
        return new MockResponse();
    }

    public MockResponse() {
    }

    public void redirectTo(String location) {
        this.location = location;
    }

    public void assertRedirectedTo(String expectedLocation) {
        assertThat("redirection", location, equalTo(expectedLocation));
    }

    public void header(String name, String value) {
        headers.put(name, value);
    }

    public void headerDate(String name, long date) {
    }

    public String header(String name) {
        return headers.get(name);
    }

    public void removeHeader(String name) {
    }

    public void assertHasHeader(String name, String value) {
        assertThat("header[" + name + "]" , header(name), equalTo(value));
    }

    public void contentType(String contentType) {
        this.contentType = contentType;
    }

    public String contentType() {
        return contentType;
    }

    public int statusCode() {
        return status.code;
    }

    public void status(HttpStatus status) {
        this.status = status;
    }

    public void statusCode(int code) {
        status(HttpStatus.forCode(code));
    }

    public void assertStatus(HttpStatus expected) {
        assertThat("response status", status, equalTo(expected));
    }

    public int contentLength() {
        return 0;
    }

    public void contentLength(int length) {
    }

    public Charset charset() {
        return null;
    }

    public OutputStream outputStream() throws IOException {
        return null;
    }

    public OutputStream outputStream(int bufferSize) throws IOException {
        return null;
    }

    public Writer writer() throws IOException {
        return null;
    }

    public void body(String body) throws IOException {
    }

    public void reset() throws IOException {
    }

    public <T> T unwrap(Class<T> type) {
        return null;
    }

    public MockResponse withContentType(String contentType) {
        contentType(contentType);
        return this;
    }

    public MockResponse withStatus(HttpStatus status) {
        status(status);
        return this;
    }
}
