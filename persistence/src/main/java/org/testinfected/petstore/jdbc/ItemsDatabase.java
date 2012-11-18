package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsDatabase implements ItemInventory {

    private final Connection connection;
    private final Table<Item> itemsTable = Tables.items();

    public ItemsDatabase(Connection connection) {
        this.connection = connection;
    }

    public List<Item> findByProductNumber(String productNumber) {
        List<Item> matches = new ArrayList<Item>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select " +
                        "item.id, item.number, item.price, item.description, item.product_id, " +
                        "product.id, product.name, product.number, product.description, product.photo_file_name " +
                    "from items item " +
                    "inner join products product on item.product_id = product.id " +
                    "where product.number = ?");
            query.setString(1, productNumber);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                matches.add(new ItemRecord().hydrate(rs));
            }
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
        return matches;
    }

    public Item find(ItemNumber itemNumber) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select " +
                            "item.id, item.number, item.price, item.description, item.product_id, " +
                            "product.id, product.name, product.number, product.description, product.photo_file_name " +
                            "from items item " +
                            "join products product on item.product_id = product.id " +
                            "where item.number = ?");
            query.setString(1, itemNumber.getNumber());
            ResultSet rs = query.executeQuery();
            rs.next();
            return new ItemRecord().hydrate(rs);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
    }

    public void add(Item item) {
        Insert.into(itemsTable, item).execute(connection);
    }

    private void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }
}
