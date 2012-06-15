package org.testinfected.petstore;

import java.net.URL;
import java.nio.charset.Charset;

public class ClassPathResourceLoader implements ResourceLoader {

    private final ClassLoader classLoader;
    private final Charset defaultCharset;

    public ClassPathResourceLoader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ClassPathResourceLoader(ClassLoader classLoader) {
        this(classLoader, Charset.defaultCharset());
    }

    public ClassPathResourceLoader(ClassLoader classLoader, Charset defaultCharset) {
        this.classLoader = classLoader;
        this.defaultCharset = defaultCharset;
    }

    public Resource load(String name) {
        URL url = classLoader.getResource(makeRelative(name));
        if (url == null) throw new ResourceNotFoundException(name);
        return new ClassPathResource(url, defaultCharset);
    }

    private String makeRelative(final String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }
}
