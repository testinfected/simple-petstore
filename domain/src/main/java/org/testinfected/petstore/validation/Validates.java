package org.testinfected.petstore.validation;

import org.testinfected.petstore.billing.CorrectCardNumber;
import org.testinfected.petstore.billing.CreditCardType;

public final class Validates {

    public static <T> NotNull<T> notNull(T value) {
        return new NotNull<>(value);
    }

    public static NotEmpty notEmpty(String value) {
        return new NotEmpty(value);
    }

    public static <T> Valid<T> validityOf(T value) {
        return new Valid<>(value);
    }

    public static CorrectCardNumber correctnessOf(CreditCardType cardType, String cardNumber) {
        return new CorrectCardNumber(cardType, cardNumber);
    }

    public static <T> Both<T> both(Constraint<T> left, Constraint<T> right) {
        return new Both<>(left, right);
    }

    private Validates() {}
}
