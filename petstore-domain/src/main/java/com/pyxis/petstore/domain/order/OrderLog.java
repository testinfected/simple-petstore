package com.pyxis.petstore.domain.order;

import com.natpryce.maybe.Maybe;

public interface OrderLog {

    Maybe<Order> find(OrderNumber orderNumber);

    void record(Order order);
}
