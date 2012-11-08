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

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
