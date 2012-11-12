package test.com.pyxis.petstore.domain.billing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.com.pyxis.petstore.matchers.SerializedForm.serializedForm;

public class CreditCardDetailsTest {

    @Test public void
    areSerializable() {
        assertThat("credit card details", validVisaDetails().build(), serializedForm(notNullValue()));
    }
}
