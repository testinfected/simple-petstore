package org.testinfected.petstore.views;

import com.samskivert.mustache.Mustache;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.lib.ErrorMessages;

import java.math.BigDecimal;

public class Checkout {

    private static final Address UNKNOWN_ADDRESS = new Address("", "", "");
    private static final CreditCardDetails MISSING_CARD_DETAILS =
            new CreditCardDetails(CreditCardType.amex, "", "", UNKNOWN_ADDRESS);

    private BigDecimal total = new BigDecimal(0);
    private CreditCardDetails details = MISSING_CARD_DETAILS;
    private ErrorMessages errors = new ErrorMessages();

    public Checkout() {}

    public Checkout forTotalOf(BigDecimal amount) {
        this.total = amount;
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Checkout withPayment(CreditCardDetails details) {
        this.details = details;
        return this;
    }

    public ChoiceOfCreditCards getAcceptedCards() {
        return ChoiceOfCreditCards.all().select(details.getCardType());
    }

    public String getCardNumber() {
        return details.getCardNumber();
    }

    public String getCardExpiryDate() {
        return details.getCardExpiryDate();
    }

    public String getFirstName() {
        return details.getFirstName();
    }

    public String getCardName() {
        return details.getCardCommonName();
    }

    public String getEmail() {
        return details.getEmail();
    }

    public String getLastName() {
        return details.getLastName();
    }

    public Checkout withErrors(ErrorMessages messages) {
        errors.addAll(messages);
        return this;
    }

    public ErrorMessages getErrorMessages() {
        return errors;
    }

    public Mustache.Lambda errors() {
        return new ValidationErrors(errors);
    }
}
