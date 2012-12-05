package org.testinfected.support;

import org.simpleframework.http.ContentType;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.support.util.Charsets;

import java.io.*;
import java.nio.charset.Charset;

import static org.simpleframework.http.Status.SEE_OTHER;

public class SimpleResponse implements org.testinfected.support.Response {

    private final Response response;
    private final RenderingEngine renderer;
    private final Charset charset;

    public SimpleResponse(Response response, RenderingEngine renderer, Charset charset) {
        this.response = response;
        this.renderer = renderer;
        this.charset = charset;
    }

    public void render(String view, Object context) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset.name().toLowerCase());
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), charset);
        renderer.render(out, view, context);
        out.flush();
    }

    public void renderHead(int statusCode) {
        response.setCode(statusCode);
        response.setText(Status.getDescription(statusCode));
    }

    public void redirectTo(String location) {
        renderHead(SEE_OTHER.getCode());
        response.set("Location", location);
    }

    public void header(String name, String value) {
        response.set(name, value);
    }

    public void headerDate(String name, long date) {
        response.setDate(name, date);
    }

    public void contentType(String mediaType) {
        header("Content-Type", mediaType);
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

    public PrintWriter writer() throws IOException {
        return new PrintWriter(new OutputStreamWriter(response.getOutputStream(), charset));
    }

    public void body(String body) throws IOException {
        Writer writer = writer();
        writer.write(body);
        writer.flush();
    }

    public Charset charset() {
        ContentType type = response.getContentType();

        if(type == null || type.getCharset() == null) {
            return Charsets.ISO_8859_1;
        }

        return Charset.forName(type.getCharset());
    }

    public OutputStream outputStream() throws IOException {
        return response.getOutputStream();
    }

    public void reset() {
        try {
            response.reset();
        } catch (IOException e) {
            throw new HttpException("Response has already been committed");
        }
    }

    public <T> T unwrap(Class<T> type) {
        if (!type.isAssignableFrom(response.getClass())) throw new IllegalArgumentException("Unsupported type: " + type.getName());
        return type.cast(response);
    }
}
