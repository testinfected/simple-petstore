package com.vtence.molecule;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

// todo Consider letting implementations decide to implement Closeable
public interface Body extends Closeable {

    long size(Charset charset);

    void writeTo(OutputStream out, Charset charset) throws IOException;
}
