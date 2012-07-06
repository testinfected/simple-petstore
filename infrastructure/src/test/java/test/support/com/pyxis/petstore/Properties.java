package test.support.com.pyxis.petstore;

import org.testinfected.hamcrest.ExceptionImposter;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static java.lang.Integer.parseInt;

// todo move to domain, along with ExceptionImposter?
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

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public int getInt(final String name) {
        return parseInt(getValue(name));
    }

    public File getFile(String key) {
        return new File(getValue(key));
    }

    public Set<String> names() {
        return properties.stringPropertyNames();
    }

    public void put(String key, String value) {
        properties.setProperty(key, value);
    }

    public java.util.Properties toJavaProperties() {
        java.util.Properties javaProperties = new java.util.Properties();
        javaProperties.putAll(properties);
        return javaProperties;
    }

    public void merge(Properties defaults) {
        for (String name : defaults.names()) {
            if (!containsKey(name)) put(name, defaults.getValue(name));
        }
    }

    public void override(Properties other) {
        for (String name : other.names()) {
            put(name, other.getValue(name));
        }
    }

    public Properties copy() {
        Properties copy = new Properties();
        copy.merge(this);
        return copy;
    }
}
