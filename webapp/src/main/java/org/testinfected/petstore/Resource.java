package org.testinfected.petstore;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface Resource {
    
    InputStream open() throws IOException;

    Reader read() throws IOException;

    String mimeType();

    long lastModified();

    int contentLength();
}
