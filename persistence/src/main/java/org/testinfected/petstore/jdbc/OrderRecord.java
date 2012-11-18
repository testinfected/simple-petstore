package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderNumber;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    public static final String TABLE = "orders";

    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String PAYMENT = "payment_id";

    private final Record<? extends PaymentMethod> payments;

    public OrderRecord(Record<? extends PaymentMethod> payments) {
        this.payments = payments;
    }

    @Override
    public String table() {
        return TABLE;
    }

    @Override
    public List<String> columns() {
        return Arrays.asList(ID, NUMBER, PAYMENT);
    }

    @Override
    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number(rs)));
        if (paymentId(rs) != null)
            order.pay(payments.hydrate(rs));
        idOf(order).set(id(rs));
        return order;
    }

    @Override
    public void dehydrate(PreparedStatement statement, Order order) throws SQLException {
        statement.setLong(indexOf(ID), idOf(order).get());
        statement.setString(indexOf(NUMBER), order.getNumber());
        if (order.isPaid()) {
            statement.setLong(indexOf(PAYMENT), idOf(order.getPaymentMethod()).get());
        } else {
            statement.setNull(indexOf(PAYMENT), Types.BIGINT);
        }

    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(getColumnIndex(rs, ID));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, NUMBER));
    }

    private String paymentId(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, PAYMENT));
    }

    private int getColumnIndex(ResultSet rs, final String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName) &&
                    metaData.getTableName(i).equalsIgnoreCase(TABLE))
                return i;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }
}
