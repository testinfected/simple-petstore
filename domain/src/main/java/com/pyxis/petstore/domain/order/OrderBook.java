package com.pyxis.petstore.domain.order;

public interface OrderBook {

    Order find(OrderNumber orderNumber);

    void record(Order order);
}
