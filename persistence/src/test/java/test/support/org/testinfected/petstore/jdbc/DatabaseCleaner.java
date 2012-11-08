package test.support.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.UnitOfWork;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseCleaner {

    private static final String[] TABLES = {
            "line_items",
            "orders",
            "payments",
            "items",
            "products",
            "order_numbers"
    };
    private final DataSource dataSource;

    public DatabaseCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clean() throws Exception {
        final Connection connection = dataSource.getConnection();
        new JDBCTransactor(connection).perform(new UnitOfWork() {
            public void execute() throws Exception {
                for (String table : TABLES) {
                    truncate(connection, table);
                }
            }
        });
        connection.close();
    }

    private void truncate(Connection connection, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("truncate table " + table);
        statement.executeUpdate();
        statement.close();
    }
}
