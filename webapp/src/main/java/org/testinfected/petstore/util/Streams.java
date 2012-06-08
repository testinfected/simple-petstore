package org.testinfected.petstore.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static byte[] toBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        copy(stream, buffer);
        return buffer.toByteArray();
    }

    private Streams() {}
}
