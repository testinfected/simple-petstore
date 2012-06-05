package org.testinfected.petstore;

import org.simpleframework.http.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Streams {

    public static void close(Response response) {
        try {
            response.close();
        } catch (IOException ignored) {
        }
    }

    public static void close(Closeable stream) {
        try {
            stream.close();
        } catch (IOException ignored) {
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

    private Streams() {}
}
