package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.order.LineItem;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.testinfected.petstore.jdbc.Properties.idOf;

// todo: Eliminate duplication in insert and select statements (see ProductsDatabase, ItemsDatabase)

public class OrdersDatabase implements OrderBook {

    private final Connection connection;

    public OrdersDatabase(Connection connection) {
        this.connection = connection;
    }

    public Order find(OrderNumber orderNumber) {
        Order order = selectOrder(orderNumber);
        List<LineItem> lineItems = selectLineItems(order);
        for (LineItem lineItem : lineItems) {
            order.addLineItem(lineItem);
        }
        return order;
    }

    private Order selectOrder(OrderNumber orderNumber) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select id, number " +
                    "from orders " +
                    "where number = ?");
            query.setString(1, orderNumber.getNumber());
            ResultSet rs = query.executeQuery();
            rs.next();
            return new OrderRecord().hydrate(rs);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
    }

    private List<LineItem> selectLineItems(Order order) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select id, item_number, item_description, item_unit_price, quantity, total_price " +
                    "from line_items " +
                    "where order_id = ? " +
                    "order by order_line");
            query.setLong(1, idOf(order).get());
            ResultSet rs = query.executeQuery();
            List<LineItem> lineItems = new ArrayList<LineItem>();
            while (rs.next()) {
                lineItems.add(new LineItemRecord().hydrate(rs));
            }
            return lineItems;
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
    }

    public void record(Order order) {
        insertOrder(order);
        insertLineItems(order);
    }

    private void insertOrder(Order order) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement("insert into orders(number) values(?)", RETURN_GENERATED_KEYS);
            insert.setString(1, order.getNumber());
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            idOf(order).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert order " + order, e);
        } finally {
            close(insert);
        }
    }

    private void insertLineItems(Order order) {
        for (int line = 0; line < order.getLineItems().size(); line++) {
            LineItem lineItem = order.getLineItems().get(line);
            insertLineItem(order, lineItem, line + 1);
        }
    }

    private void insertLineItem(Order order, LineItem lineItem, int line) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(
                    "insert into line_items(item_number, item_unit_price, item_description, quantity, total_price, order_id, order_line) " +
                            "values(?, ?, ?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            insert.setString(1, lineItem.getItemNumber());
            insert.setBigDecimal(2, lineItem.getItemUnitPrice());
            insert.setString(3, lineItem.getItemDescription());
            insert.setInt(4, lineItem.getQuantity());
            insert.setBigDecimal(5, lineItem.getTotalPrice());
            insert.setLong(6, idOf(order).get());
            insert.setInt(7, line);
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            idOf(lineItem).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert line item " + lineItem, e);
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
