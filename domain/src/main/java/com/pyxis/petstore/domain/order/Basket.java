package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.product.Item;

public interface Basket {

    void add(Item item);
}
