package org.testinfected.petstore.billing;

import java.io.Serializable;

public class CreditCardDetails extends PaymentMethod implements Serializable {

    private final CreditCardType cardType;
    private final String cardNumber;
    private final String cardExpiryDate;
    private final Address billingAddress;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate, Address billingAddress) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.billingAddress = billingAddress;
    }

    public CreditCardType getCardType() {
        return cardType;
    }

    public String getCardCommonName() {
        return cardType.commonName();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public String getFirstName() {
        return billingAddress.getFirstName();
    }

    public String getLastName() {
        return billingAddress.getLastName();
    }

    public String getEmail() {
        return billingAddress.getEmailAddress();
    }
}
