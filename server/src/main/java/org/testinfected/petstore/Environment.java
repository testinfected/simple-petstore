package org.testinfected.petstore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Environment {

    private static final String FILE_LOCATION = "etc/%s.properties";

    public static Properties test() throws IOException {
        return load("test");
    }

    public static Properties load(String name) throws IOException {
        String resource = fileLocation(name);
        InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        if (config == null) throw new IOException("Environment file not found: " + resource);
        Properties props = new Properties();
        props.load(config);
        return overrideWithSystemProperties(props);
    }

    private static String fileLocation(String name) {
        return String.format(FILE_LOCATION, name);
    }

    private static Properties overrideWithSystemProperties(Properties properties) {
        properties.putAll(System.getProperties());
        return properties;
    }
}
