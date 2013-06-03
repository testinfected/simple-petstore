package org.testinfected.petstore.db;

import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.product.Product;

public class Access {

    public static FieldAccessor<Long> idOf(Object entity) {
        return FieldAccessor.access(entity, "id");
    }

    public static FieldAccessor<Product> productOf(Object entity) {
        return FieldAccessor.access(entity, "product");
    }

    public static FieldAccessor<Order> orderOf(Object entity) {
        return FieldAccessor.access(entity, "order");
    }

    private Access() {}
}


