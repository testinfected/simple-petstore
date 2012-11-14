package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.ExceptionImposter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductsDatabase implements ProductCatalog {

    private final Connection connection;

    public ProductsDatabase(Connection connection) {
        this.connection = connection;
    }

    public Product findByNumber(String productNumber) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select id, name, number, description, photo_file_name " +
                    "from products where number = ?");
            query.setString(1, productNumber);
            ResultSet resultSet = query.executeQuery();

            resultSet.next();
            return new ProductRecord().hydrate(resultSet);
        } catch (SQLException e) {
            throw ExceptionImposter.imposterize(e);
        } finally {
            close(query);
        }
    }

    public void add(Product product) {
        insert(product);
    }

    private void insert(Product product) {
        new Insert(product).execute(connection);
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
                Product product = new ProductRecord().hydrate(resultSet);
                matches.add(product);
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
