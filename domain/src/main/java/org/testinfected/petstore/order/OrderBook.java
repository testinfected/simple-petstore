package org.testinfected.petstore.order;

public interface OrderBook {

    Order find(OrderNumber orderNumber);

    void record(Order order);
}
