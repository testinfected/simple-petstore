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
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class BufferedResponse extends ResponseWrapper {

    // todo remove eventually and use a byte buffer
    // Note: we have to keep delegating as long as responses are still unwrapped during their lifecycle
    private final BufferedSimpleResponse buffer;

    public BufferedResponse(Response response) {
        super(response);
        buffer = new BufferedSimpleResponse(response.unwrap(org.simpleframework.http.Response.class));
    }

    public void reset() throws IOException {
        buffer.reset();
    }

    public OutputStream outputStream() throws IOException {
        return buffer.getOutputStream();
    }

    public OutputStream outputStream(int bufferSize) throws IOException {
        return buffer.getOutputStream(bufferSize);
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
        return buffer.getContent();
    }

    public <T> T unwrap(Class<T> type) {
        return (T) buffer;
    }

    public static class BufferedSimpleResponse extends org.simpleframework.http.ResponseWrapper {

        private final Buffer buffer;

        public BufferedSimpleResponse(org.simpleframework.http.Response response) {
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

        public WritableByteChannel getByteChannel() throws IOException {
            return buffer;
        }

        public WritableByteChannel getByteChannel(int size) throws IOException {
            return buffer;
        }

        public byte[] getContent() {
            return buffer.toByteArray();
        }

        private static class Buffer extends ByteArrayOutputStream implements WritableByteChannel {

            public int write(ByteBuffer src) throws IOException {
                byte[] bytes = src.array();
                write(bytes);
                return bytes.length;
            }

            public boolean isOpen() {
                return true;
            }
        }
    }
}
