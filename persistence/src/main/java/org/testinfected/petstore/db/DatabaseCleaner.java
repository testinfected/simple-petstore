package org.testinfected.petstore.db;

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
        try (Connection connection = dataSource.getConnection()) {
            new JDBCTransactor(connection).perform(() -> {
                for (String table : TABLES) {
                    delete(connection, table);
                }
            });
        }
    }

    private void delete(Connection connection, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from " + table);
        statement.executeUpdate();
        statement.close();
    }
}
