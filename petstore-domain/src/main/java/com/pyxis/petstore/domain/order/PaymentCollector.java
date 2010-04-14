package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.CreditCardDetails;

public interface PaymentCollector {

    void collectPayment(Order order, CreditCardDetails creditCardDetails);
}
