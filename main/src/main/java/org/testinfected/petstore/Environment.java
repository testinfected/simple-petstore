package org.testinfected.petstore;

import org.testinfected.petstore.jdbc.DataSourceProperties;
import org.testinfected.petstore.util.PropertyFile;

import java.util.Properties;

public final class Environment {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String FILE_LOCATION = "etc/%s.properties";

    public static Environment load(String name) {
        return new Environment(PropertyFile.load(propertyFile(name)));
    }

    private static String propertyFile(String name) {
        return String.format(FILE_LOCATION, name);
    }

    public final DataSourceProperties database;

    public Environment(Properties properties) {
        database = readDataSourceProperties(overrideWithSystemProperties(properties));
    }

    private DataSourceProperties readDataSourceProperties(Properties props) {
        return new DataSourceProperties(
                    props.getProperty(JDBC_URL),
                    props.getProperty(JDBC_USERNAME),
                    props.getProperty(JDBC_PASSWORD));
    }

    private Properties overrideWithSystemProperties(Properties properties) {
        properties.putAll(System.getProperties());
        return properties;
    }
}
