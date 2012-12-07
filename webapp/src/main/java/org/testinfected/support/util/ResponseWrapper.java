package org.testinfected.support.util;

import org.testinfected.support.HttpStatus;
import org.testinfected.support.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

public class ResponseWrapper implements Response {

    private final Response response;

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public void redirectTo(String location) {
        response.redirectTo(location);
    }

    public String header(String name) {
        return response.header(name);
    }

    public void header(String name, String value) {
        response.header(name, value);
    }

    public void headerDate(String name, long date) {
        response.headerDate(name, date);
    }

    public void removeHeader(String name) {
        response.removeHeader(name);
    }

    public void contentType(String contentType) {
        response.contentType(contentType);
    }

    public String contentType() {
        return response.contentType();
    }

    public int statusCode() {
        return response.statusCode();
    }

    public void status(HttpStatus status) {
        response.status(status);
    }

    public void statusCode(int code) {
        response.statusCode(code);
    }

    public int contentLength() {
        return response.contentLength();
    }

    public void contentLength(int length) {
        response.contentLength(length);
    }

    public Charset charset() {
        return response.charset();
    }

    public OutputStream outputStream() throws IOException {
        return response.outputStream();
    }

    public OutputStream outputStream(int bufferSize) throws IOException {
        return response.outputStream(bufferSize);
    }

    public Writer writer() throws IOException {
        return response.writer();
    }

    public void body(String body) throws IOException {
        response.body(body);
    }

    public void reset() throws IOException {
        response.reset();
    }

    public <T> T unwrap(Class<T> type) {
        return response.unwrap(type);
    }
}
