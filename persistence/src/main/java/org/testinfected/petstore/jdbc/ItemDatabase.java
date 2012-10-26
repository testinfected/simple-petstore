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
import java.util.Random;

import static org.testinfected.petstore.jdbc.DatabaseIdentifier.idOf;

public class ItemDatabase implements ItemInventory {

    private final Connection connection;

    public ItemDatabase(Connection connection) {
        this.connection = connection;
    }

    public List<Item> findByProductNumber(String productNumber) {
        List<Item> matches = new ArrayList<Item>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("select i.number from items i, products p where i.product_id = p.id and p.number = ?");
            query.setString(1, productNumber);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                matches.add(new Item(null, new Product(productNumber, null), null));
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
            insert = connection.prepareStatement("insert into items(number, product_id, price) values(?, ?, ?)");
            Random random = new Random();
            insert.setString(1, String.valueOf(random.nextInt(100000000)));
            insert.setLong(2, idOf(item.getProduct()).get());
            insert.setBigDecimal(3, item.getPrice());
            executeInsert(insert);
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
