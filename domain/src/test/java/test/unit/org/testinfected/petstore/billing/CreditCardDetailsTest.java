package test.unit.org.testinfected.petstore.billing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.matchers.SerializedForm.serializedForm;

public class CreditCardDetailsTest {

    @Test public void
    areSerializable() {
        assertThat("credit card details", validVisaDetails().build(), serializedForm(notNullValue()));
    }
}
