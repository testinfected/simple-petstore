package org.testinfected.petstore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class ClassPathResourceLoader implements ResourceLoader {

    private final ClassLoader classLoader;

    public ClassPathResourceLoader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ClassPathResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public URL load(String resource) {
        URL url = classLoader.getResource(resource);
        if (url == null) throw new ResourceNotFoundException("classpath:" + resource);
        return url;
    }

    public InputStream stream(String resource) throws IOException {
        return load(resource).openStream();
    }

    public Reader read(String name, String charsetName) throws IOException {
        return new InputStreamReader(stream(name), charsetName);
    }
}
