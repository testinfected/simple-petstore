package org.testinfected.petstore;

import org.testinfected.petstore.util.PropertyFile;

import java.util.Properties;

public final class Environment {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String FILE_LOCATION = "etc/%s.properties";

    public static Environment load(String name) {
        return new Environment(overrideWithSystemProperties(PropertyFile.load(fileLocation(name))));
    }

    private static String fileLocation(String name) {
        return String.format(FILE_LOCATION, name);
    }

    private static Properties overrideWithSystemProperties(Properties properties) {
        properties.putAll(System.getProperties());
        return properties;
    }

    public final String databaseUrl;
    public final String databaseUsername;
    public final String databasePassword;

    public Environment(Properties properties) {
        databaseUrl = properties.getProperty(JDBC_URL);
        databaseUsername = properties.getProperty(JDBC_USERNAME);
        databasePassword = properties.getProperty(JDBC_PASSWORD);
    }
}
