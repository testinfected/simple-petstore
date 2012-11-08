package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderNumber;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;

// todo: Eliminate duplication in record classes
public class OrderRecord {

    private static final String ORDERS_TABLE = "orders";

    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number(rs)));
        idOf(order).set(id(rs));
        return order;
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(getColumnIndex(rs, "id"));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "number"));
    }

    private int getColumnIndex(ResultSet rs, final String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName) &&
                    metaData.getTableName(i).equalsIgnoreCase(ORDERS_TABLE))
                return i;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }
}
