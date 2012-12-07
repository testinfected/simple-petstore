package org.testinfected.support.decoration;

import org.testinfected.support.Response;
import org.testinfected.support.ResponseWrapper;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class BufferedResponse extends ResponseWrapper {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public BufferedResponse(Response response) {
        super(response);
    }

    public void reset() throws IOException {
        buffer.reset();
    }

    public OutputStream outputStream() throws IOException {
        return buffer;
    }

    public OutputStream outputStream(int bufferSize) throws IOException {
        return buffer;
    }

    public Writer writer() throws IOException {
        return new OutputStreamWriter(outputStream(), charset());
    }

    public void body(String body) throws IOException {
        Writer writer = new BufferedWriter(writer());
        writer.write(body);
        writer.flush();
    }

    public String body() throws UnsupportedEncodingException {
        return new String(content(), charset());
    }

    public byte[] content() {
        return buffer.toByteArray();
    }
}
