package org.testinfected.petstore;

import java.io.File;
import java.nio.charset.Charset;

public class FileSystemResourceLoader implements ResourceLoader {

    private final File base;
    final Charset defaultCharset;

    public FileSystemResourceLoader(File base) {
        this(base, Charset.defaultCharset());
    }

    public FileSystemResourceLoader(File base, Charset defaultCharset) {
        this.base = base;
        this.defaultCharset = defaultCharset;
    }

    public Resource load(String name) {
        File file = new File(base, name);
        if (!file.exists()) throw new ResourceNotFoundException(name);
        return new FileResource(file, defaultCharset);
    }
}
