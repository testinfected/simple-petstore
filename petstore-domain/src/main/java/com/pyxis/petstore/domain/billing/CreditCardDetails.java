package com.pyxis.petstore.domain.billing;

import javax.persistence.*;

@Entity @Access(AccessType.FIELD) @Table(name = "payments")
public class CreditCardDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

    @Enumerated(EnumType.STRING)
    private CreditCardType cardType;
    private String cardNumber;
    private String cardExpiryDate;

    @Embedded @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name")),
        @AttributeOverride(name = "emailAddress", column = @Column(name = "billing_email"))
    })
    private Address billingAddress;

    CreditCardDetails() {}

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate, Address billingAddress) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.billingAddress = billingAddress;
    }

    public CreditCardType getCardType() {
        return cardType;
    }

    public void setCardType(CreditCardType cardType) {
        this.cardType = cardType;
    }

    public String getCardCommonName() {
        return cardType.getCommonName();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
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
