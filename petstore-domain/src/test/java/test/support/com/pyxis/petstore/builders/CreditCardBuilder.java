package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.CreditCard;

public class CreditCardBuilder implements Builder<CreditCard> {

    private CreditCard.Type cardType;
    private String cardNumber;
    private String cardExpiryDate;

    public static CreditCardBuilder aVisa() {
        return aCreditCard().ofType(CreditCard.Type.visa);
    }

    public static CreditCardBuilder aCreditCard() {
        return new CreditCardBuilder();
    }

    public CreditCard build() {
        return new CreditCard(cardType, cardNumber, cardExpiryDate);
    }

    public CreditCardBuilder ofType(CreditCard.Type type) {
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
}
