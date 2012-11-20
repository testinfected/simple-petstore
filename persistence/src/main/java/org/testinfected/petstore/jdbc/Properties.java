package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.product.Product;

public class Properties {

    public static Property<Long> idOf(Object entity) {
        return new Property<Long>(entity, "id");
    }

    public static Property<Product> productOf(Object entity) {
        return new Property<Product>(entity, "product");
    }

    public static Property<Order> orderOf(Object entity) {
        return new Property<Order>(entity, "order");
    }
}


