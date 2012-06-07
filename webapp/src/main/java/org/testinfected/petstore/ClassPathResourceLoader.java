package org.testinfected.petstore;

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
}
