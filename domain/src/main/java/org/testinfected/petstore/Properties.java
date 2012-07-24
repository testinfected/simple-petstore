package org.testinfected.petstore;

import java.io.IOException;

public class Properties {

    public static Properties load(String name) {
        return load(name, Thread.currentThread().getContextClassLoader());
    }

    public static Properties load(String name, ClassLoader classLoader) {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(classLoader.getResourceAsStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Properties(properties);
    }

    private final java.util.Properties properties;

    public Properties() {
        this(new java.util.Properties());
    }

    public Properties(java.util.Properties properties) {
        this.properties = properties;
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public void put(String key, String value) {
        properties.setProperty(key, value);
    }
}
