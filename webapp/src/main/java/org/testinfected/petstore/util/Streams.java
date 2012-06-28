package org.testinfected.petstore.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class Streams {

    private static final int SIZE_4K = 4 * 1024;

    public static void close(Closeable stream) {
        try {
            if (stream != null) stream.close();
        } catch (IOException ignored) {
        }
    }

    public static void copy(InputStream source, OutputStream destination) throws IOException {
        byte[] buffer = new byte[SIZE_4K];
        int read;
        while ((read = source.read(buffer)) != -1) {
            destination.write(buffer, 0, read);
        }
    }

    public static String toString(InputStream in) throws IOException {
        return toString(in, Charset.defaultCharset().name());
    }

    public static String toString(InputStream in, String encoding) throws IOException {
        return new String(toBytes(in), encoding);
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        copy(in, buffer);
        return buffer.toByteArray();
    }

    private Streams() {
    }
}
