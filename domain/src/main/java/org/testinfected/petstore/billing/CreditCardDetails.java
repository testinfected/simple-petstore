package org.testinfected.petstore.billing;

import org.testinfected.petstore.validation.Constraint;
import org.testinfected.petstore.validation.NotNull;
import org.testinfected.petstore.validation.Valid;

import java.io.Serializable;

import static org.testinfected.petstore.validation.Validate.both;
import static org.testinfected.petstore.validation.Validate.correct;
import static org.testinfected.petstore.validation.Validate.notEmpty;
import static org.testinfected.petstore.validation.Validate.notNull;
import static org.testinfected.petstore.validation.Validate.valid;

public class CreditCardDetails extends PaymentMethod implements Serializable {

    private final CreditCardType cardType;
    private final Constraint<String> cardNumber;
    private final NotNull<String> cardExpiryDate;
    private final Valid<Address> billingAddress;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate, Address billingAddress) {
        this.cardType = cardType;
        this.cardNumber = both(notEmpty(cardNumber), correct(cardType, cardNumber));
        this.cardExpiryDate = notNull(cardExpiryDate);
        this.billingAddress = valid(billingAddress);
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
