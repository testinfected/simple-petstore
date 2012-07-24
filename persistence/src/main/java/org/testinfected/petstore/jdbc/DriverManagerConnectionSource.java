package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.ExceptionImposter;
import org.testinfected.petstore.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerConnectionSource implements ConnectionSource {

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private final String url;
    private final String username;
    private final String password;

    public static DriverManagerConnectionSource configure(Properties properties) {
        return new DriverManagerConnectionSource(
                properties.getString(JDBC_URL),
                properties.getString(JDBC_USERNAME),
                properties.getString(JDBC_PASSWORD));
    }

    public DriverManagerConnectionSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection connect() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}
