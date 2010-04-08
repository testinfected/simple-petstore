package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.billing.Account;
import com.pyxis.petstore.domain.billing.CreditCard;

import javax.validation.Valid;

public class PaymentDetails {
    private final Account billingAccount = new Account();

    private String cardType;
    private String cardNumber;
    private String cardExpiryDate;

    @Valid public Account getBillingAccount() {
        return billingAccount;
    }

    @Valid public CreditCard getCreditCard() {
        return new CreditCard(CreditCard.Type.valueOf(cardType), cardNumber, cardExpiryDate);
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }
}
