package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.product.Product;
import org.testinfected.petstore.jdbc.support.Property;

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


