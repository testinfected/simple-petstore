package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.order.LineItem;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.jdbc.records.LineItemRecord;
import org.testinfected.petstore.jdbc.records.OrderRecord;
import org.testinfected.petstore.jdbc.records.PaymentRecord;
import org.testinfected.petstore.jdbc.support.Insert;
import org.testinfected.petstore.jdbc.support.Select;

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
        Order order = findOrder(orderNumber);
        addLinesToOrder(order, findLineItemsOf(order));
        return order;
    }

    private void addLinesToOrder(Order order, List<LineItem> lineItems) {
        for (LineItem lineItem : lineItems) {
            order.addLineItem(lineItem);
        }
    }

    private Order findOrder(OrderNumber orderNumber) {
        return Select.from(orders, "_order").
                leftJoin(payments, "payment", "_order.payment_id = payment.id").
                where("_order.number = ?", orderNumber).
                first(connection);
    }

    private List<LineItem> findLineItemsOf(Order order) {
        return Select.from(lineItems).
                where("order_id = ?", idOf(order).get()).
                orderBy("order_line").
                list(connection);
    }

    public void record(Order order) {
        recordPayment(order);
        recordOrder(order);
        recordLineItems(order);
    }

    private void recordPayment(Order order) {
        if (!order.isPaid()) return;
        Insert.into(payments, order.getPaymentMethod()).execute(connection);
    }

    private void recordOrder(Order order) {
        Insert.into(orders, order).execute(connection);
    }

    private void recordLineItems(Order order) {
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
