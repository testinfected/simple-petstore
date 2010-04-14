package com.pyxis.petstore.domain.billing;

import javax.persistence.*;

@Embeddable
public class CreditCardDetails {

    @Enumerated(EnumType.STRING)
    private CreditCardType type;
    private String number;
    private String expiryDate;

    @Embedded @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name")),
        @AttributeOverride(name = "emailAddress", column = @Column(name = "billing_email"))
    })
    private Address billingAddress;

    CreditCardDetails() {}

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate, Address billingAddress) {
        this.type = cardType;
        this.number = cardNumber;
        this.expiryDate = cardExpiryDate;
        this.billingAddress = billingAddress;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public static CreditCardDetails visa(String cardNumber, String cardExpiryDate, Address billingAddress) {
        return new CreditCardDetails(CreditCardType.visa, cardNumber, cardExpiryDate, billingAddress);
    }
}
