package org.testinfected.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public interface Response {

    void render(String view, Object context) throws IOException;

    void redirectTo(String location);

    void renderHead(int statusCode);

    void header(String name, String value);

    void headerDate(String name, long date);

    void contentType(String contentType);

    int statusCode();

    void status(HttpStatus status);

    void statusCode(int code);

    void statusText(String reason);

    int contentLength();

    void contentLength(int length);

    Charset charset();

    OutputStream outputStream() throws IOException;

    PrintWriter writer() throws IOException;

    void body(String body) throws IOException;

    void reset() throws IOException;

    <T> T unwrap(Class<T> type);
}
