package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.UnitOfWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseCleaner {

    private static final String[] TABLES = {
            "items",
            "products"
    };
    private final Connection connection;

    public DatabaseCleaner(Connection connection) {
        this.connection = connection;
    }

    public void clean() throws Exception {
        new JDBCTransactor(connection).perform(new UnitOfWork() {
            public void execute() throws Exception {
                for (String table : TABLES) {
                    truncate(table);
                }
            }
        });
    }

    private void truncate(String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("truncate table " + table);
        statement.executeUpdate();
        statement.close();
    }
}
