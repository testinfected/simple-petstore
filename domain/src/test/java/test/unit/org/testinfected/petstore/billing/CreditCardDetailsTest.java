package test.unit.org.testinfected.petstore.billing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.matchers.SerializedForm.serializedForm;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.on;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.succeeds;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.validationOf;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.violates;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.withMessage;

public class CreditCardDetailsTest {

    String MISSING = null;
    String EMPTY = "";
    String BLANK = "   ";

    @Test public void
    areSerializable() {
        assertThat("card details", validVisaDetails().build(), describedAs("are serializable", serializedForm(notNullValue())));
    }

    @Test public void
    areInvalidWithAMissingOrBlankCardNumber() {
        assertThat("validation of card with missing number", validationOf(aVisa().withNumber(MISSING)), violates(on("cardNumber"), withMessage("blank")));
        assertThat("validation of card with empty number", validationOf(aVisa().withNumber(EMPTY)), violates(on("cardNumber"), withMessage("blank")));
        assertThat("validation of card with blank number", validationOf(aVisa().withNumber(BLANK)), violates(on("cardNumber"), withMessage("blank")));
    }

    @Test public void
    areInvalidWithoutACardExpiryDate() {
        assertThat("validation of card with missing expiry date", validationOf(aVisa().withExpiryDate(MISSING)), violates(on("cardExpiryDate"), withMessage("missing")));
    }

    @Test public void
    areInvalidWithAnInvalidAddress() {
        assertThat("validation of card with missing first name", validationOf(aVisa().billedTo(anAddress().withFirstName(MISSING))), violates(on("billingAddress.firstName"), withMessage("missing")));
        assertThat("validation of card with missing last name", validationOf(aVisa().billedTo(anAddress().withLastName(MISSING))), violates(on("billingAddress.lastName"), withMessage("missing")));
        assertThat("validation of card with invalid address", validationOf(aVisa().billedTo(anAddress().withFirstName(MISSING))), violates(on("billingAddress"), withMessage("invalid")));
    }

    @Test public void
    areValidWithAllRequiredDetails() {
        assertThat("validation of valid card", validationOf(validVisaDetails()), succeeds());
    }
}
