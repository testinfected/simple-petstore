package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.PaymentMethod;

public interface PaymentCollector {

    void collectPayment(Order order, PaymentMethod paymentMethod);
}
