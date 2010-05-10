package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.Maybe;

public interface OrderLog {

    Maybe<Order> find(OrderNumber orderNumber);

    void record(Order order);
}
