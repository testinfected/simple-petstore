package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.CreditCardType;

import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;

public class CreditCardBuilder implements Builder<CreditCardDetails> {

    private CreditCardType cardType = CreditCardType.visa;
    private String cardNumber = "4111111111111111";
    private String cardExpiryDate = "12/26";
    private Address billingAddress = anAddress().build();

    public static CreditCardBuilder validCreditCardDetails() {
        return aVisa();
    }

    public static CreditCardBuilder aCreditCard() {
        return new CreditCardBuilder();
    }

    public static CreditCardBuilder aVisa() {
        return aCreditCard().ofType(CreditCardType.visa);
    }

    public CreditCardBuilder ofType(CreditCardType type) {
        this.cardType = type;
        return this;
    }

    public CreditCardBuilder withNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public CreditCardBuilder withExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
        return this;
    }

    public CreditCardBuilder billedTo(AddressBuilder addressBuilder) {
        return billedTo(addressBuilder.build());
    }

    public CreditCardBuilder billedTo(Address billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public CreditCardBuilder but() {
        CreditCardBuilder but = new CreditCardBuilder();
        but.ofType(this.cardType);
        but.withNumber(this.cardNumber);
        but.withExpiryDate(this.cardExpiryDate);
        but.billedTo(this.billingAddress);
        return but;
    }

    public CreditCardDetails build() {
        return new CreditCardDetails(cardType, cardNumber, cardExpiryDate, billingAddress);
    }
}
