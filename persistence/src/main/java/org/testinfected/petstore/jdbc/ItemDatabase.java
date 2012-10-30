package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;
import com.pyxis.petstore.domain.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;
import static org.testinfected.petstore.jdbc.Properties.productOf;

public class ItemDatabase implements ItemInventory {

    private final Connection connection;

    public ItemDatabase(Connection connection) {
        this.connection = connection;
    }

    public List<Item> findByProductNumber(String productNumber) {
        List<Item> matches = new ArrayList<Item>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select item.id, item.number, item.price, item.description, item.product_id " +
                    "from items item " +
                    "join products product on item.product_id = product.id " +
                    "where product.number = ?");
            query.setString(1, productNumber);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                Product product = new Product(productNumber, null);
                Item item = new Item(new ItemNumber(rs.getString("number")), product, rs.getBigDecimal("price"));
                item.setDescription(rs.getString("description"));
                idOf(item).set(rs.getLong("id"));
                matches.add(item);
            }
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);

        }
        return matches;
    }

    public Item find(ItemNumber itemNumber) {
        return null;
    }

    public void add(Item item) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement("insert into items(number, product_id, price, description) values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insert.setString(1, item.getNumber());
            insert.setLong(2, idOf(productOf(item).get()).get());
            insert.setBigDecimal(3, item.getPrice());
            insert.setString(4, item.getDescription());
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            idOf(item).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert item: " + item, e);
        } finally {
            close(insert);
        }
    }

    private void executeInsert(PreparedStatement insert) throws SQLException {
        int rowsInserted = insert.executeUpdate();
        if (rowsInserted != 1) {
            throw new SQLException("Unexpected row count: " + rowsInserted + "; expected: 1");
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
