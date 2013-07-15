package test.unit.org.testinfected.petstore.billing;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.billing.CorrectCardNumber;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.validation.Constraint;

import static org.junit.Assert.assertThat;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.fails;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.succeeds;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.validationOf;

public class CorrectCardNumberTest {

    String MISSING = null;

    @Test public void
    rejectsMissingNumbers() {
        assertInvalid(visa(MISSING), "missing");
    }

    @Test public void
    recognizesVisaNumbers() {
        assertInvalid(visa("5111111111111111"), "not starting with 4");
        assertValid(visa("4111111111111"), "13 digits starting with 4");
        assertValid(visa("4111111111111111"), "16 digits starting with 4");
        assertInvalid(visa("41111111"), "less than 13 digits");
        assertInvalid(visa("4111111111111111111111"), "more than 16 digits");
        assertInvalid(visa("41111111111111"), "only 14 digits");
    }

    @Test public void
    recognizesMasterCardNumbers() {
        assertInvalid(mastercard("4111111111111111"), "not starting with 5");
        assertValid(mastercard(  "5111111111111111"), "16 digits starting with 51");
        assertValid(mastercard(  "5211111111111111"), "16 digits starting with 52");
        assertValid(mastercard(  "5311111111111111"), "16 digits starting with 53");
        assertValid(mastercard(  "5411111111111111"), "16 digits starting with 54");
        assertValid(mastercard(  "5511111111111111"), "16 digits starting with 55");
        assertInvalid(mastercard("51111111"), "less than 16 digits");
        assertInvalid(mastercard("5111111111111111111111"), "more than 16 digits");
        assertInvalid(mastercard("5911111111111111"), "not starting with 5[1-5]");
    }

    @Test public void
    recognizesAmericanExpressNumbers() {
        assertValid(amex(  "341111111111111"), "15 digits starting with 34");
        assertInvalid(amex("351111111111111"), "not starting with 34 or 37");
        assertInvalid(amex("3411111111111111"), "more than 15 digits");
        assertInvalid(amex("3411111111111"), "less than 15 digits");
        assertValid(amex(  "371111111111111"), "15 digits starting with 37");
    }

    private void assertValid(CorrectCardNumber cardNumber, String message) {
        assertValidationOf(message, cardNumber, succeeds());
    }

    private void assertInvalid(CorrectCardNumber cardNumber, String message) {
        assertValidationOf(message, cardNumber, fails());
    }

    private void assertValidationOf(String message, CorrectCardNumber cardNumber, Matcher<Iterable<?>> matcher) {
        assertThat(message, validationOf(Target.with(cardNumber)), matcher);
    }

    public CorrectCardNumber visa(String cardNumber) {
        return new CorrectCardNumber(CreditCardType.visa, cardNumber);
    }

    public CorrectCardNumber mastercard(String cardNumber) {
        return new CorrectCardNumber(CreditCardType.mastercard, cardNumber);
    }

    private CorrectCardNumber amex(String cardNumber) {
        return new CorrectCardNumber(CreditCardType.amex, cardNumber);
    }

    public static class Target<T> {

        public static <T> Target<T> with(Constraint<T> cardNumber) {
            return new Target<T>(cardNumber);
        }

        private final Constraint<T> cardNumber;

        public Target(Constraint<T> cardNumber) {
            this.cardNumber = cardNumber;
        }
    }
}
