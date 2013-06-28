package test.unit.org.testinfected.petstore.billing;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.petstore.ConstraintViolation;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.billing.CreditCardDetails;

import java.util.Set;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyIterable;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.matchers.SerializedForm.serializedForm;

public class CreditCardDetailsTest {

    String MISSING = null;
    String EMPTY = "";
    String BLANK = "   ";

    Validator validator = new Validator();

    @Test public void
    areSerializable() {
        assertThat("card details", validCard(), describedAs("are serializable", serializedForm(notNullValue())));
    }

    @Test public void
    areInvalidWithAMissingOrBlankCardNumber() {
        assertThat("validation of card with missing number", validationOf(cardWithNumber(MISSING)), violates(on("cardNumber"), withMessage("blank")));
        assertThat("validation of card with empty number", validationOf(cardWithNumber(EMPTY)), violates(on("cardNumber"), withMessage("blank")));
        assertThat("validation of card with blank number", validationOf(cardWithNumber(BLANK)), violates(on("cardNumber"), withMessage("blank")));
    }

    @Test public void
    areInvalidWithoutACardExpiryDate() {
        assertThat("validation of card with missing expiry date", validationOf(cardWithExpiryDate(MISSING)), violates(on("cardExpiryDate"), withMessage("missing")));
    }

    @Test public void
    areValidWithAllRequiredDetails() {
        assertThat("validation of valid card", validationOf(validCard()), succeeds());
    }

    @Test(expected = IllegalArgumentException.class) public void
    throwsIllegalArgumentExceptionWhenUsedWithInvalidCreditCardNumber() {
        cardWithNumber(BLANK).getCardNumber();
    }

    @Test(expected = IllegalArgumentException.class) public void
    throwsIllegalArgumentExceptionWhenUsedWithInvalidExpiryDate() {
        cardWithExpiryDate(MISSING).getCardExpiryDate();
    }

    private Matcher<Iterable<?>> succeeds() {
        return Matchers.describedAs("succeeds", emptyIterable());
    }

    private Set<ConstraintViolation<?>> validationOf(CreditCardDetails details) {
        return validator.validate(details);
    }

    private Matcher<Iterable<? extends ConstraintViolation<?>>> violates(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher) {
        return Matchers.contains(violation(pathMatcher, messageMatcher));
    }

    private Matcher<ConstraintViolation<?>> violation(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher) {
        return Matchers.allOf(pathMatcher, messageMatcher);
    }

    private CreditCardDetails cardWithNumber(String cardNumber) {
        return validVisaDetails().withNumber(cardNumber).build();
    }

    private CreditCardDetails cardWithExpiryDate(String date) {
        return validVisaDetails().withExpiryDate(date).build();
    }

    private CreditCardDetails validCard() {
        return validVisaDetails().build();
    }

    public static Matcher<ConstraintViolation<?>> on(String path) {
        return new FeatureMatcher<ConstraintViolation<?>, String>(equalTo(path), "on path", "path") {
            @Override protected String featureValueOf(ConstraintViolation<?> actual) {
                return actual.path();
            }
        };
    }

    public static Matcher<ConstraintViolation<?>> withMessage(String message) {
        return new FeatureMatcher<ConstraintViolation<?>, String>(containsString(message), "with message", "message") {
            @Override protected String featureValueOf(ConstraintViolation<?> actual) {
                return actual.message();
            }
        };
    }
}
