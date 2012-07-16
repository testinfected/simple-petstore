package org.testinfected.petstore.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class Buffer extends ByteArrayOutputStream implements WritableByteChannel {

    public int write(ByteBuffer src) throws IOException {
        byte[] bytes = src.array();
        write(bytes);
        return bytes.length;
    }

    public boolean isOpen() {
        return true;
    }
}
