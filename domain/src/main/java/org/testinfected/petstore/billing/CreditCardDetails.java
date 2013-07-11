package org.testinfected.petstore.billing;

import org.testinfected.petstore.validation.NotBlank;
import org.testinfected.petstore.validation.NotNull;
import org.testinfected.petstore.validation.Valid;
import org.testinfected.petstore.validation.Validate;

import java.io.Serializable;

public class CreditCardDetails extends PaymentMethod implements Serializable {

    private final CreditCardType cardType;
    private final NotBlank cardNumber;
    private final NotNull<String> cardExpiryDate;
    private final Valid<Address> billingAddress;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate, Address billingAddress) {
        this.cardType = cardType;
        this.cardNumber = Validate.notBlank(cardNumber);
        this.cardExpiryDate = Validate.notNull(cardExpiryDate);
        this.billingAddress = Validate.valid(billingAddress);
    }

    public CreditCardType cardType() {
        return cardType;
    }

    public String cardCommonName() {
        return cardType.commonName();
    }

    public String cardNumber() {
        return cardNumber.get();
    }

    public String cardExpiryDate() {
        return cardExpiryDate.get();
    }

    public String firstName() {
        return billingAddress.get().firstName();
    }

    public String lastName() {
        return billingAddress.get().lastName();
    }

    public String email() {
        return billingAddress.get().emailAddress();
    }
}
