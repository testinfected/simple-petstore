package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.util.PropertyFile;

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
        return new TestDatabaseEnvironment(PropertyFile.load(DATABASE_PROPERTIES));
    }

    public TestDatabaseEnvironment(Properties properties) {
        this.url = properties.getProperty(JDBC_URL);
        this.username = properties.getProperty(JDBC_USERNAME);
        this.password = properties.getProperty(JDBC_PASSWORD);
    }
}
