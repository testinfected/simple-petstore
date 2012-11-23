package org.testinfected.petstore.jdbc.records;

import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.jdbc.support.Column;
import org.testinfected.petstore.jdbc.support.Record;
import org.testinfected.petstore.jdbc.support.Table;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderNumber;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    private final Table orders = Table.named("orders");
    private final Column<Long> id = orders.LONG("id");
    private final Column<String> number = orders.STRING("number");
    private final Column<Long> payment = orders.LONG("payment_id");
    private final Record<? extends PaymentMethod> payments;

    public OrderRecord(Record<? extends PaymentMethod> payments) {
        this.payments = payments;
    }

    @Override
    public Table table() {
        return orders;
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
