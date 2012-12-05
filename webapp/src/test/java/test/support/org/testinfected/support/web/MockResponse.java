package test.support.org.testinfected.support.web;

import org.testinfected.support.HttpStatus;
import org.testinfected.support.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class MockResponse implements Response {

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

    public void contentType(String contentType) {
    }

    public int statusCode() {
        return 0;
    }

    public void status(HttpStatus status) {
    }

    public void statusCode(int code) {
    }

    public void statusText(String reason) {
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

    public PrintWriter writer() throws IOException {
        return null;
    }

    public void body(String body) throws IOException {
    }

    public void reset() throws IOException {
    }

    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
