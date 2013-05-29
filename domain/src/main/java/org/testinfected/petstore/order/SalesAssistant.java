package org.testinfected.petstore.order;

import org.testinfected.petstore.billing.PaymentMethod;

import java.math.BigDecimal;

public interface SalesAssistant {

    Iterable<CartItem> orderContent();

    BigDecimal orderTotal();

    OrderNumber placeOrder(PaymentMethod paymentMethod) throws Exception;
}
