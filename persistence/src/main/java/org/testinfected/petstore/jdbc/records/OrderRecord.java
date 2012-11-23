package org.testinfected.petstore.jdbc.records;

import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.jdbc.support.Column;
import org.testinfected.petstore.jdbc.support.Table;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderNumber;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    private final Table<? extends PaymentMethod> payments;

    private final Table<Order> orders = new Table<Order>("orders", this);

    private final Column<Long> id = orders.LONG("id");
    private final Column<String> number = orders.STRING("number");
    private final Column<Long> payment = orders.LONG("payment_id");

    public static Table<Order> buildTable(Table<? extends PaymentMethod> payments) {
        return new OrderRecord(payments).orders;
    }

    public OrderRecord(Table<? extends PaymentMethod> payments) {
        this.payments = payments;
    }

    @Override
    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number.get(rs)));
        if (payment.get(rs) != Types.NULL)
            order.pay(payments.hydrate(rs));
        idOf(order).set(id.get(rs));
        return order;
    }

    @Override
    public void dehydrate(PreparedStatement st, Order order) throws SQLException {
        id.set(st, idOf(order).get());
        number.set(st, order.getNumber());
        payment.set(st, order.isPaid() ? idOf(order.getPaymentMethod()).get() : null);
    }
}
