package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.jdbc.ConnectionSource;
import org.testinfected.petstore.jdbc.DriverManagerConnectionSource;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    public static Database configure() {
        return configure(DatabaseConfiguration.load());
    }

    public static Database configure(DatabaseConfiguration config) {
        return new Database(new DriverManagerConnectionSource(config.getUrl(), config.getUsername(), config.getPassword()));
    }

    private final ConnectionSource connectionSource;

    private Connection connection;

    public Database(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public Connection connect() {
        connection = connectionSource.connect();
        return connection;
    }

    public void transaction(UnitOfWork work) throws Exception {
        new JDBCTransactor(connection).perform(work);
    }

    public void clean() throws Exception {
        new DatabaseCleaner(connection).clean();
    }

    public void close() throws SQLException {
        connection.close();
    }
}
