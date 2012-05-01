package test.support.com.pyxis.petstore;

import org.testinfected.hamcrest.ExceptionImposter;

import java.io.IOException;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class Properties {

    public static Properties system() {
        return new Properties(System.getProperties());
    }

    public static Properties load(String name) {
        return load(name, Thread.currentThread().getContextClassLoader());
    }

    public static Properties load(String name, ClassLoader classLoader) {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(classLoader.getResourceAsStream(name));
        } catch (IOException e) {
            throw ExceptionImposter.imposterize(e);
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

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public int getValueAsInt(final String name) {
        return parseInt(getValue(name));
    }

    public Set<String> names() {
        return properties.stringPropertyNames();
    }

    public java.util.Properties toJavaProperties() {
        java.util.Properties javaProperties = new java.util.Properties();
        javaProperties.putAll(properties);
        return javaProperties;
    }

    public void merge(Properties defaults) {
        for (String name : defaults.names()) {
            if (!properties.containsKey(name)) properties.setProperty(name, defaults.getValue(name));
        }
    }

    public void override(Properties other) {
        for (String name : other.names()) {
            properties.setProperty(name, other.getValue(name));
        }
    }

}
