package org.testinfected.petstore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Streams {

    private Streams() {
    }

    public static void close(Closeable stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable... streams) {
        for (Closeable stream : streams) {
            close(stream);
        }
    }

    public static void copy(InputStream source, OutputStream destination) throws IOException {
        InputStream in = new BufferedInputStream(source);
        OutputStream out = new BufferedOutputStream(destination);
        while (true) {
            int data = in.read();
            if (data == -1) break;
            out.write(data);
        }
        out.flush();
    }
}
