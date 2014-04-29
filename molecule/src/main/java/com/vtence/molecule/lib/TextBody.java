package com.vtence.molecule.lib;

import com.vtence.molecule.Body;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class TextBody implements Body {

    private final StringBuilder content = new StringBuilder();

    public static TextBody text(String text) {
        return new TextBody().append(text);
    }

    public TextBody() {}

    public TextBody append(CharSequence text) {
        this.content.append(text);
        return this;
    }

    public String text() {
        return content.toString();
    }

    public long size(Charset charset) {
        return content(charset).length;
    }

    private byte[] content(Charset charset) {
        return text().getBytes(charset);
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        out.write(content(charset));
    }

    public void close() throws IOException {
    }
}
