package org.testinfected.petstore.util;

import org.simpleframework.http.ContentType;
import org.simpleframework.http.Response;
import org.simpleframework.http.ResponseWrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.WritableByteChannel;

public class BufferedResponse extends ResponseWrapper {

    private final Buffer buffer;

    public BufferedResponse(Response response) {
        super(response);
        buffer = new Buffer();
    }

    public void reset() throws IOException {
        buffer.reset();
    }

    public OutputStream getOutputStream() throws IOException {
        return buffer;
    }

    public OutputStream getOutputStream(int size) throws IOException {
        return buffer;
    }

    public PrintStream getPrintStream() throws IOException {
        return getPrintStream(0);
    }

    public PrintStream getPrintStream(int size) throws IOException {
        return new PrintStream(getOutputStream(), false, getCharset());
    }

    public WritableByteChannel getByteChannel() throws IOException {
        return buffer;
    }

    public WritableByteChannel getByteChannel(int size) throws IOException {
        return buffer;
    }

    public String getBody() throws UnsupportedEncodingException {
        return new String(getContent(), getCharset());
    }

    public byte[] getContent() {
        return buffer.toByteArray();
    }

    public void flush() throws IOException {
        byte[] content = getContent();
        setContentLength(content.length);
        response.getOutputStream(content.length).write(content);
    }

    private String getCharset() {
        ContentType type = getContentType();

        if(type == null || type.getCharset()==null) {
            return Charsets.ISO_8859_1.name();
        }

        return type.getCharset();
    }
}
