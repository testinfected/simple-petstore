package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.Account;
import com.pyxis.petstore.domain.billing.CreditCard;

public interface PaymentCollector {

    void collectPayment(Order order, CreditCard creditCard, Account billingAccount);
}
