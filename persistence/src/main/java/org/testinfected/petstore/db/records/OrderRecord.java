package org.testinfected.petstore.db.records;

import com.vtence.tape.Column;
import com.vtence.tape.Record;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderNumber;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.testinfected.petstore.db.Access.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<Long> paymentId;

    private final Record<? extends PaymentMethod> payments;

    public OrderRecord(Column<Long> id, Column<String> number, Column<Long> paymentId, Record<? extends PaymentMethod> payments) {
        this.id = id;
        this.number = number;
        this.paymentId = paymentId;
        this.payments = payments;
    }

    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number.get(rs)));
        if (paymentId.get(rs) != Types.NULL)
            order.pay(payments.hydrate(rs));
        idOf(order).set(id.get(rs));
        return order;
    }

    public void dehydrate(PreparedStatement st, Order order) throws SQLException {
        id.set(st, idOf(order).get());
        number.set(st, order.getNumber());
        paymentId.set(st, order.isPaid() ? idOf(order.getPaymentMethod()).get() : null);
    }
}
