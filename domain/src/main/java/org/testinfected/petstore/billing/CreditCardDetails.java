package org.testinfected.petstore.billing;

import org.testinfected.petstore.Check;
import org.testinfected.petstore.NotBlank;
import org.testinfected.petstore.NotNull;

import java.io.Serializable;

public class CreditCardDetails extends PaymentMethod implements Serializable {

    private final CreditCardType cardType;
    private final NotBlank cardNumber;
    private final NotNull<String> cardExpiryDate;
    private final Address billingAddress;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate, Address billingAddress) {
        this.cardType = cardType;
        this.cardNumber = Check.notBlank(cardNumber);
        this.cardExpiryDate = Check.notNull(cardExpiryDate);
        this.billingAddress = billingAddress;
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
        return billingAddress.getFirstName();
    }

    public String getLastName() {
        return billingAddress.getLastName();
    }

    public String getEmail() {
        return billingAddress.getEmailAddress();
    }
}
