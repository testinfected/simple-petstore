package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Insert {
    private Product product;

    public Insert(Product product) {
        this.product = product;
    }

    public void execute(final Connection connection) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement("insert into products(name, description, photo_file_name, number) values(?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            insert.setString(1, product.getName());
            insert.setString(2, product.getDescription());
            insert.setString(3, product.hasPhoto() ? product.getPhotoFileName() : null);
            insert.setString(4, product.getNumber());
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            Properties.idOf(product).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert product " + product, e);
        } finally {
            close(insert);
        }
    }

    private void executeInsert(PreparedStatement insert) throws SQLException {
        int rowsInserted = insert.executeUpdate();
        if (rowsInserted != 1) {
            throw new SQLException("Unexpected row count of " + rowsInserted + "; expected was 1");
        }
    }

    private void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }
}
