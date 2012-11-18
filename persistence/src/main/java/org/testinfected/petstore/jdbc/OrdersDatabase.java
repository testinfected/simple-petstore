package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.order.LineItem;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;

import java.sql.*;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;
import static org.testinfected.petstore.jdbc.Properties.orderOf;

public class OrdersDatabase implements OrderBook {

    private final Connection connection;
    private final PaymentRecord payments = new PaymentRecord();
    private final OrderRecord orders = new OrderRecord(payments);
    private final LineItemRecord lineItems = new LineItemRecord();

    public OrdersDatabase(Connection connection) {
        this.connection = connection;
    }

    public Order find(OrderNumber orderNumber) {
        Order order = selectOrderAndPayment(orderNumber);
        List<LineItem> lineItems = selectLineItems(order);
        for (LineItem lineItem : lineItems) {
            order.addLineItem(lineItem);
        }
        return order;
    }

    private Order selectOrderAndPayment(OrderNumber orderNumber) {
        Select<Order> select = Select.from(orders, "_order");
        select.leftJoin(payments, "payment", "_order.payment_id = payment.id");
        select.where("_order.number = ?", orderNumber);
        return select.single(connection);
    }

    private List<LineItem> selectLineItems(Order order) {
        Select<LineItem> select = Select.from(lineItems);
        select.where("order_id = ?", idOf(order).get());
        select.orderBy("order_line");
        return select.list(connection);
    }

    public void record(Order order) {
        insertPayment(order);
        insertOrder(order);
        insertLineItems(order);
    }

    private void insertPayment(Order order) {
        if (!order.isPaid()) return;
        Insert.into(payments, order.getPaymentMethod()).execute(connection);
    }

    private void insertOrder(Order order) {
        Insert.into(orders, order).execute(connection);
    }

    private void insertLineItems(Order order) {
        for (LineItem lineItem : order.getLineItems()) {
            associate(order, lineItem);
            insertLineItem(lineItem);
        }
    }

    private void associate(Order order, LineItem lineItem) {
        orderOf(lineItem).set(order);
    }

    private void insertLineItem(LineItem lineItem) {
        Insert.into(lineItems, lineItem).execute(connection);
    }
}
