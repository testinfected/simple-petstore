package org.testinfected.petstore.order;

import org.testinfected.petstore.billing.PaymentMethod;

public interface SalesAssistant {

    OrderNumber placeOrder(Cart cart, PaymentMethod paymentMethod) throws Exception;
}
