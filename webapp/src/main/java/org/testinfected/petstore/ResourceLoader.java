package org.testinfected.petstore;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public interface ResourceLoader {

    URL load(String resource);

    InputStream stream(String resource) throws IOException;

    Reader read(String name, String charsetName) throws IOException;
}
