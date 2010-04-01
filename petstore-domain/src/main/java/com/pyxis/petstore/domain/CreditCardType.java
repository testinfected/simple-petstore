package com.pyxis.petstore.domain;

public enum CreditCardType {

    visa("Visa"), mastercard("MasterCard"), amex("American Express");
    private final String commonName;

    CreditCardType(String commonName) {
        this.commonName = commonName;
    }

    public String getCommonName() {
        return commonName;
    }

    @Override public String toString() {
        return name();
    }
}
