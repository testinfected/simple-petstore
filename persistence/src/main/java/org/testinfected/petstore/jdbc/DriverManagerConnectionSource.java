package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.ExceptionImposter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerConnectionSource implements ConnectionSource {

    private final String url;
    private final String username;
    private final String password;

    public DriverManagerConnectionSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}
