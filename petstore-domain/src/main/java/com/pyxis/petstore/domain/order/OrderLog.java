package com.pyxis.petstore.domain.order;

public interface OrderLog {

    Order find(OrderNumber orderNumber);

    void record(Order order);
}
