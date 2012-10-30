package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

public class Properties {

    public static Property<Long> idOf(Object entity) {
        return new Property<Long>(entity, "id");
    }

    public static Property<Product> productOf(Object entity) {
        return new Property<Product>(entity, "product");
    }
}


