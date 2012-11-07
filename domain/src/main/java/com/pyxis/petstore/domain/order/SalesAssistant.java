package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.product.ItemNumber;

import java.math.BigDecimal;

public interface SalesAssistant {

    void addToCart(ItemNumber itemNumber);

    Iterable<CartItem> orderContent();

    BigDecimal orderTotal();

    OrderNumber placeOrder(PaymentMethod paymentMethod) throws Exception;
}
