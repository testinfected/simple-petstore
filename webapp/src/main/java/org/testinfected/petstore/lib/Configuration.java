package org.testinfected.petstore.lib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

public class Configuration {

    private final Properties props;

    public static Configuration load(String resource) throws IOException {
        InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        if (config == null) throw new FileNotFoundException("Configuration file not found: " + resource);
        Properties props = new Properties();
        props.load(config);
        return new Configuration(props);
    }

    public Configuration(Properties props) {
        this.props = props;
    }

    public void merge(Properties properties) {
        this.props.putAll(properties);
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public URL getURL(String key) {
        try {
            return new URL(get(key));
        } catch (MalformedURLException e) {
            throw new Problem(String.format("'%s' is not a valid url", key), e);
        }
    }

    public Integer getInt(String key) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            throw new Problem(String.format("'%s' is not a valid integer", key), e);
        }
    }

    public Integer getInt(String key, int defaultValue) {
        Integer value = getInt(key);
        return value != null ? value : defaultValue;
    }

    public void set(String key, String value) {
        this.props.setProperty(key, value);
    }

    public Set<String> keys() {
        return props.stringPropertyNames();
    }

    /**
     * This exception is thrown when there is a configuration problem.
     */
    public class Problem extends RuntimeException {
        public Problem(String explanation, Throwable cause) {
            super(explanation, cause);
        }
    }
}
