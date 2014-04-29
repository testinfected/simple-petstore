package com.vtence.molecule.lib;

import com.vtence.molecule.Body;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class BinaryBody implements Body {

    private static final Body EMPTY = bytes(new byte[0]);

    private final byte[] content;

    public static Body empty() {
        return EMPTY;
    }

    public static Body bytes(byte[] content) {
        return new BinaryBody(content);
    }

    public BinaryBody(byte[] content) {
        this.content = content;
    }

    public long size(Charset charset) {
        return content.length;
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        out.write(content);
    }

    public void close() throws IOException {
    }
}
