package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.jdbc.DataSourceProperties;
import org.testinfected.petstore.util.PropertyFile;

import java.util.Properties;

public class TestEnvironment {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String DATABASE_PROPERTIES = "database.properties";

    public static DataSourceProperties properties() {
        Properties props = PropertyFile.load(DATABASE_PROPERTIES);

        return new DataSourceProperties(
                props.getProperty(JDBC_URL),
                props.getProperty(JDBC_USERNAME),
                props.getProperty(JDBC_PASSWORD));
    }
}
