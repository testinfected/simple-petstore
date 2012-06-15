package org.testinfected.petstore;

import org.testinfected.petstore.util.MimeTypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class FileResource implements Resource {
    private final File file;
    private final Charset defaultCharset;

    public FileResource(File file, Charset defaultCharset) {
        this.file = file;
        this.defaultCharset = defaultCharset;
    }

    public InputStream open() throws IOException {
        return new FileInputStream(file);
    }

    public Reader read() throws IOException {
        return new InputStreamReader(open(), defaultCharset);
    }

    public String mimeType() {
        return MimeTypes.guessFrom(file.getName());
    }

    public long lastModified() {
        return file.lastModified();
    }

    public int contentLength() {
        return (int) file.length();
    }
}
