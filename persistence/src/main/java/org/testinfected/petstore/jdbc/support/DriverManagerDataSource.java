package org.testinfected.petstore.jdbc.support;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DriverManagerDataSource implements DataSource {

    private final String url;
    private final String username;
    private final String password;

    public DriverManagerDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(username, password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        return connection;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return new PrintWriter(new OutputStream() {
            public void write(int b) throws IOException {
            }
        });
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (!isDataSource(iface)) throw new IllegalArgumentException(iface.getName());
        return (T) this;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return isDataSource(iface);
    }

    private boolean isDataSource(Class<?> iface) {
        return DataSource.class.equals(iface);
    }

    // This is for compatibility with JDK 1.7
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("This data source does not use logging");
    }
}
