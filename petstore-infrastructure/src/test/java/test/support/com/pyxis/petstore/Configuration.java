package test.support.com.pyxis.petstore;

import org.testinfected.hamcrest.ExceptionImposter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class Configuration {

    public static Configuration load(String name) {
        return load(name, Thread.currentThread().getContextClassLoader());
    }

    public static Configuration load(String name, ClassLoader classLoader) {
        Properties properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream(name));
        } catch (IOException e) {
            throw ExceptionImposter.imposterize(e);
        }
        return new Configuration(properties);
    }

    private final Properties properties;

    public Configuration(Properties properties) {
        this.properties = properties;
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public int getValueAsInt(final String name) {
        return parseInt(getValue(name));
    }

    public Properties toProperties() {
        return new Properties(properties);
    }

    public Set<String> names() {
        return properties.stringPropertyNames();
    }
}
