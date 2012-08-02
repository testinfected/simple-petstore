package test.support.com.pyxis.petstore.web;

import java.io.IOException;
import java.util.Properties;

public class TestEnvironment {

    public static final String SERVER_PORT = "server.port";

    private static final String TEST_PROPERTIES = "test.properties";

    public static TestEnvironment load() {
        return load(TEST_PROPERTIES);
    }

    public static TestEnvironment load(String resource) {
        return new TestEnvironment(loadConfiguration(resource));
    }

    private static Properties loadConfiguration(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Properties props = new Properties();
        try {
            props.load(classLoader.getResourceAsStream(resource));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load test configuration file: " + resource, e);
        }
        return props;
    }

    public final Properties properties;

    public TestEnvironment(Properties properties) {
        this.properties = properties;
        properties.putAll(System.getProperties());
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty(SERVER_PORT));
    }
}
