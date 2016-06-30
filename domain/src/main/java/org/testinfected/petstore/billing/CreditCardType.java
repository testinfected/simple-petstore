package org.testinfected.petstore.billing;

public enum CreditCardType {
    visa("Visa"), mastercard("MasterCard"), amex("American Express");

    private final String commonName;

    CreditCardType(String commonName) {
        this.commonName = commonName;
    }

    public String commonName() {
        return commonName;
    }

    public String toString() {
        return name();
    }
}
