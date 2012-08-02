package test.support.org.testinfected.petstore.jdbc;

import java.io.IOException;
import java.util.Properties;

public class DatabaseConfiguration {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String DATABASE_PROPERTIES = "database.properties";

    public static DatabaseConfiguration load() {
        return new DatabaseConfiguration(loadConfigurationFile());
    }

    private static Properties loadConfigurationFile() {
        Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DATABASE_PROPERTIES));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load database configuration file: " + DATABASE_PROPERTIES);
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
