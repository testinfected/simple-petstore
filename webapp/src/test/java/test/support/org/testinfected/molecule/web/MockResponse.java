package test.support.org.testinfected.molecule.web;

import org.hamcrest.Matcher;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MockResponse implements Response {

    private final Map<String, String> headers = new HashMap<String, String>();
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private HttpStatus status;

    public static MockResponse aResponse() {
        return new MockResponse();
    }

    public MockResponse() {
    }

    public void redirectTo(String location) {
        header("Location", location);
    }

    public void assertRedirectedTo(String expectedLocation) {
        assertThat("redirection", header("Location"), equalTo(expectedLocation));
    }

    public void header(String name, String value) {
        headers.put(name, value);
    }

    public void headerDate(String name, long date) {
        header(name, formatDate(date));
    }

    public String header(String name) {
        return headers.get(name);
    }

    public void removeHeader(String name) {
    }

    public void assertHeader(String name, String value) {
        assertHeader(name, equalTo(value));
    }

    public void assertHeader(String name, Matcher<? super String> valueMatcher) {
        assertThat("header[" + name + "]" , header(name), valueMatcher);
    }

    public void contentType(String contentType) {
        header("Content-Type", contentType);
    }

    public String contentType() {
        return header("Content-Type");
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
        return Integer.parseInt(header("Content-Length"));
    }

    public void contentLength(int length) {
        header("Content-Length", String.valueOf(length));
    }

    public Charset charset() {
        return Charset.defaultCharset();
    }

    public OutputStream outputStream() throws IOException {
        return output;
    }

    public OutputStream outputStream(int bufferSize) throws IOException {
        return output;
    }

    public Writer writer() throws IOException {
        return new OutputStreamWriter(output);
    }

    public void body(String body) throws IOException {
        Writer writer = writer();
        writer.write(body);
        writer.flush();
    }

    public void assertContent(String content) {
        assertContent(equalTo(content));
    }

    public void assertContent(Matcher<? super String> contentMatcher) {
        assertThat("content", new String(output.toByteArray(), charset()), contentMatcher);
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

    public String toString() {
        return output.toString();
    }

    private static final String RFC_1123_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private String formatDate(long date) {
        SimpleDateFormat httpDate = new SimpleDateFormat(RFC_1123_DATE_FORMAT);
        httpDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDate.format(new Date(date));
    }
}
