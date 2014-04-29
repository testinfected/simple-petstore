package com.vtence.molecule.lib;

import com.vtence.molecule.Body;
import com.vtence.molecule.helpers.Streams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class FileBody implements Body {
    private static final int SIZE_8K = 8 * 1024;

    private final File file;
    private final int chunkSize;

    public FileBody(File file) {
        this(file, SIZE_8K);
    }

    public FileBody(File file, int chunkSize) {
        this.file = file;
        this.chunkSize = chunkSize;
    }

    public File file() {
        return file;
    }

    public long size(Charset charset) {
        return file.length();
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            Streams.copy(in, out, chunkSize);
        } finally {
            Streams.close(in);
        }
    }

    public void close() throws IOException {
    }
}
