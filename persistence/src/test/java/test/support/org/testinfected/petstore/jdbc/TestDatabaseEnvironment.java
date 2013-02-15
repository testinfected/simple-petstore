package test.support.org.testinfected.petstore.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestDatabaseEnvironment {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String DATABASE_PROPERTIES = "database.properties";

    public final String url;
    public final String username;
    public final String password;

    public static TestDatabaseEnvironment load() {
        try {
            InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(DATABASE_PROPERTIES);
            if (config == null) throw new IOException("Database properties not found: " + DATABASE_PROPERTIES);
            Properties props = new Properties();
            props.load(config);
            return new TestDatabaseEnvironment(props);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public TestDatabaseEnvironment(Properties properties) {
        this.url = properties.getProperty(JDBC_URL);
        this.username = properties.getProperty(JDBC_USERNAME);
        this.password = properties.getProperty(JDBC_PASSWORD);
    }
}
