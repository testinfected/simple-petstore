package org.testinfected.petstore.billing;

import org.testinfected.petstore.NotBlank;
import org.testinfected.petstore.NotNull;
import org.testinfected.petstore.Valid;
import org.testinfected.petstore.Validate;

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

    public CreditCardType getCardType() {
        return cardType;
    }

    public String getCardCommonName() {
        return cardType.commonName();
    }

    public String getCardNumber() {
        return cardNumber.get();
    }

    public String getCardExpiryDate() {
        return cardExpiryDate.get();
    }

    public String getFirstName() {
        return billingAddress.get().getFirstName();
    }

    public String getLastName() {
        return billingAddress.get().getLastName();
    }

    public String getEmail() {
        return billingAddress.get().getEmailAddress();
    }
}
