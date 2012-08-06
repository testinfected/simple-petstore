package test.support.org.testinfected.petstore.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseProperties {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String DATABASE_PROPERTIES = "database.properties";

    public static DatabaseProperties load() {
        return load(DATABASE_PROPERTIES);
    }

    private static DatabaseProperties load(final String name) {
        return new DatabaseProperties(loadProperties(name));
    }

    private static Properties loadProperties(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream config = classLoader.getResourceAsStream(resource);
        if (config == null) throw new IllegalArgumentException("Database configuration file not found: " + resource);

        Properties props = new Properties();
        try {
            props.load(config);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load database configuration file: " + resource);
        }
        return props;
    }

    private Properties properties;

    public DatabaseProperties(Properties properties) {
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
