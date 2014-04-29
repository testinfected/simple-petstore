package com.vtence.molecule.support;

import java.io.File;
import java.net.URL;

public class ResourceLocator {

    public static File locateOnClasspath(String resource) {
        return onClasspath().locate(resource);
    }

    public static ResourceLocator onClasspath() {
        return new ResourceLocator(Thread.currentThread().getContextClassLoader());
    }

    private final ClassLoader classLoader;

    public ResourceLocator(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public File locate(String resource) {
        URL fileLocation = classLoader.getResource(resource);
        if (fileLocation == null) throw new IllegalArgumentException("Cannot find " + resource);
        File target;
        try {
            target = new File(fileLocation.toURI());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return target;
    }
}
