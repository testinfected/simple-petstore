package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.product.ItemNumber;

public interface SalesAssistant {

    void addToCart(ItemNumber itemNumber);

    Cart cartContent();

    OrderNumber placeOrder(PaymentMethod paymentMethod);
}
