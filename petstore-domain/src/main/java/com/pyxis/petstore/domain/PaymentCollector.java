package com.pyxis.petstore.domain;

public interface PaymentCollector {

    void collectPayment(Order order, CreditCard creditCard, Account billingAccount);
}
