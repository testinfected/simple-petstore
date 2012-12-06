package test.support.org.testinfected.support.web;

import org.testinfected.support.HttpStatus;
import org.testinfected.support.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

public class MockResponse implements Response {

    private String contentType;
    private HttpStatus status;

    public static MockResponse aResponse() {
        return new MockResponse();
    }

    public MockResponse() {
    }

    public void render(String view, Object context) throws IOException {
    }

    public void redirectTo(String location) {
    }

    public void renderHead(int statusCode) {
    }

    public void header(String name, String value) {
    }

    public void headerDate(String name, long date) {
    }

    public void removeHeader(String name) {
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
