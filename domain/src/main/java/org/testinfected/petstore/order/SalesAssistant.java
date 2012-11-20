package org.testinfected.petstore.order;

import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.product.ItemNumber;

import java.math.BigDecimal;

public interface SalesAssistant {

    void addToCart(ItemNumber itemNumber);

    Iterable<CartItem> orderContent();

    BigDecimal orderTotal();

    OrderNumber placeOrder(PaymentMethod paymentMethod) throws Exception;
}
