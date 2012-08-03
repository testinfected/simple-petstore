package test.support.org.testinfected.petstore.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfiguration {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String DATABASE_PROPERTIES = "database.properties";

    public static DatabaseConfiguration load() {
        return load(DATABASE_PROPERTIES);
    }

    private static DatabaseConfiguration load(final String name) {
        return new DatabaseConfiguration(loadConfiguration(name));
    }

    private static Properties loadConfiguration(String resource) {
        Properties props = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream config = classLoader.getResourceAsStream(resource);
        if (config == null) throw new IllegalArgumentException("Database configuration file not found: " + resource);
        try {
            props.load(config);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load database configuration file: " + resource);
        }
        return props;
    }

    private Properties properties;

    public DatabaseConfiguration(Properties properties) {
        this.properties = properties;
    }

    public String getUrl() {
        return properties.getProperty(JDBC_URL);
    }

    public String getUsername() {
        return properties.getProperty(JDBC_USERNAME);
    }

    public String getPassword() {
        return properties.getProperty(JDBC_PASSWORD);
    }
}
