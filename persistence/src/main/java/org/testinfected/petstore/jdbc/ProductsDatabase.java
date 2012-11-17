package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDatabase implements ProductCatalog {

    private final Connection connection;
    private final Table productsTable = new Table("products");
    {
        productsTable.addColumn(Column.bigint("id"));
        productsTable.addColumn(Column.varchar("number"));
        productsTable.addColumn(Column.varchar("name"));
        productsTable.addColumn(Column.varchar("description"));
        productsTable.addColumn(Column.varchar("photo_file_name"));
    }

    public ProductsDatabase(Connection connection) {
        this.connection = connection;
    }

    public Product findByNumber(String productNumber) {
        Select select = Select.from(productsTable);
        select.where("number", productNumber);
        return select.single(connection);
    }

    public void add(Product product) {
        Insert.into(productsTable, product).execute(connection);
    }

    public List<Product> findByKeyword(String keyword) {
        List<Product> matches = new ArrayList<Product>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select id, name, number, description, photo_file_name " +
                    "from products " +
                    "where lower(name) like ? " +
                    "or lower(description) like ?");
            query.setString(1, matchAnywhere(keyword));
            query.setString(2, matchAnywhere(keyword));

            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                matches.add(productsTable.readRecord(resultSet));
            }
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
        return matches;
    }

    private void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }

    private String matchAnywhere(final String pattern) {
        return "%" + pattern + "%";
    }

}
