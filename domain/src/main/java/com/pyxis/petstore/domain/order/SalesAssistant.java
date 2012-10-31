package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.product.ItemNumber;

public interface SalesAssistant {

    void addToCart(ItemNumber itemNumber);
}
