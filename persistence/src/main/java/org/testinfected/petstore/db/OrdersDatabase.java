package org.testinfected.petstore.db;

import com.vtence.tape.Insert;
import com.vtence.tape.Select;
import com.vtence.tape.Table;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.order.LineItem;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;

import java.sql.Connection;
import java.util.List;

import static org.testinfected.petstore.db.Access.idOf;
import static org.testinfected.petstore.db.Access.orderOf;

public class OrdersDatabase implements OrderBook {

    private final Connection connection;
    private final Table<LineItem> lineItems = Schema.lineItems();
    private final Table<PaymentMethod> payments = Schema.payments();
    private final Table<Order> orders = Schema.orders(payments);

    public OrdersDatabase(Connection connection) {
        this.connection = connection;
    }

    public Order find(OrderNumber orderNumber) {
        Order order = findOrder(orderNumber);
        addLinesToOrder(order, findLineItemsOf(order));
        return order;
    }

    private void addLinesToOrder(Order order, List<LineItem> lineItems) {
        lineItems.forEach(order::addLineItem);
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
