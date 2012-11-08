package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.Maybe;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// todo: Eliminate duplication in insert and select statements (see ProductDatabase, ItemDatabase)
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class OrderDatabase implements OrderBook {

    private final Connection connection;

    public OrderDatabase(Connection connection) {
        this.connection = connection;
    }

    public Maybe<Order> find(OrderNumber orderNumber) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select id, number " +
                    "from orders " +
                    "where number = ?");
            query.setString(1, orderNumber.getNumber());
            ResultSet rs = query.executeQuery();
            rs.next();
            return Maybe.some(new OrderRecord().hydrate(rs));
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
    }

    public void record(Order order) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement("insert into orders(number) values(?)", RETURN_GENERATED_KEYS);
            insert.setString(1, order.getNumber());
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            Properties.idOf(order).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert order " + order, e);
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
