package test.com.pyxis.petstore.domain.billing;

import com.pyxis.petstore.domain.billing.CreditCardDetails;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.testinfected.hamcrest.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.com.pyxis.petstore.matchers.SerializedForm.serializedForm;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class CreditCardDetailsTest {

    String SHOULD_NOT_BE_EMPTY = "{org.hibernate.validator.constraints.NotEmpty.message}";

    @SuppressWarnings("unchecked")
    @Test public void
    areInvalidWithoutACardNumber() {
        assertThat("constraint violations", validationOf(detailsMissingCardNumber()), violates(on("cardNumber"), withError(SHOULD_NOT_BE_EMPTY)));
        assertThat("constraint violations", validationOf(detailsWithEmptyCardNumber()), violates(on("cardNumber"), withError(SHOULD_NOT_BE_EMPTY)));
    }
    
    @Test public void
    areSerializable() {
        assertThat("credit card details", validVisaDetails().build(), serializedForm(notNullValue()));
    }

    private CreditCardDetails detailsWithEmptyCardNumber() {
        return aVisa().withNumber("").build();
    }

    private CreditCardDetails detailsMissingCardNumber() {
        return aVisa().build();
    }
}
