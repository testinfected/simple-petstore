package org.testinfected.molecule.simple;

import org.simpleframework.http.ContentType;
import org.simpleframework.http.Response;
import org.simpleframework.http.parse.ContentParser;
import org.testinfected.molecule.HttpException;
import org.testinfected.molecule.HttpStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class SimpleResponse implements org.testinfected.molecule.Response {

    private final Response response;
    private final Charset defaultCharset;

    public SimpleResponse(Response response, Charset defaultCharset) {
        this.response = response;
        this.defaultCharset = defaultCharset;
    }

    public void redirectTo(String location) {
        status(HttpStatus.SEE_OTHER);
        header("Location", location);
    }

    public void header(String name, String value) {
        response.set(name, value);
    }

    public void headerDate(String name, long date) {
        response.setDate(name, date);
    }

    public void removeHeader(String name) {
        response.remove(name);
    }

    public void cookie(String name, String value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void contentType(String mediaType) {
        header("Content-Type", mediaType);
    }

    public String contentType() {
        return header("Content-Type");
    }

    public String header(String name) {
        return response.getValue(name);
    }

    public int contentLength() {
        return response.getContentLength();
    }

    public void contentLength(int length) {
        response.setContentLength(length);
    }

    public int statusCode() {
        return response.getCode();
    }

    public void status(HttpStatus status) {
        statusCode(status.code);
        statusText(status.text);
    }

    public void statusCode(int code) {
        response.setCode(code);
    }

    public void statusText(String reason) {
        response.setText(reason);
    }

    public Writer writer() throws IOException {
        return new OutputStreamWriter(outputStream(), charset());
    }

    public void body(String body) throws IOException {
        Writer writer = new BufferedWriter(writer());
        writer.write(body);
        writer.flush();
    }

    public Charset charset() {
        ContentType type = contentType() != null ? new ContentParser(contentType()) : null;

        if (type == null || type.getCharset() == null) {
            return defaultCharset;
        }

        return Charset.forName(type.getCharset());
    }

    public OutputStream outputStream() throws IOException {
        return response.getOutputStream();
    }

    public OutputStream outputStream(int bufferSize) throws IOException {
        return response.getOutputStream(bufferSize);
    }

    public void reset() {
        try {
            response.reset();
        } catch (IOException e) {
            throw new HttpException("Response has already been committed", e);
        }
    }

    public <T> T unwrap(Class<T> type) {
        if (!type.isAssignableFrom(response.getClass())) throw new IllegalArgumentException("Unsupported type: " + type.getName());
        return type.cast(response);
    }
}
