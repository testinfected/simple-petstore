package com.pyxis.petstore.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class CreditCard {

    @Enumerated(EnumType.STRING)
    private Type type;
    private String number;
    private String expiryDate;

    CreditCard() {}

    public CreditCard(Type cardType, String cardNumber, String cardExpiryDate) {
        this.type = cardType;
        this.number = cardNumber;
        this.expiryDate = cardExpiryDate;
    }

    public String getNumber() {
        return number;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getType() {
        return type.getCommonName();
    }

    public static enum Type {
        visa("Visa"), mastercard("MasterCard"), amex("American Express");

        private final String commonName;

        Type(String commonName) {
            this.commonName = commonName;
        }

        public String getCommonName() {
            return commonName;
        }

        @Override public String toString() {
            return name();
        }
    }

    public static CreditCard visa(String cardNumber, String cardExpiryDate) {
        return new CreditCard(Type.visa, cardNumber, cardExpiryDate);
    }
}
