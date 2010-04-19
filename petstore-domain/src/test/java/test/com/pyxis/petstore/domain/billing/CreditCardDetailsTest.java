package test.com.pyxis.petstore.domain.billing;

import com.pyxis.petstore.domain.billing.CreditCardDetails;
import org.junit.Test;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class CreditCardDetailsTest {

    String SHOULD_NOT_BE_EMPTY = "{org.hibernate.validator.constraints.NotEmpty.message}";

    @Test public void
    areInvalidWithoutACardNumber() {
        assertThat(validationOf(detailsMissingCardNumber()), violates(on("cardNumber"), withError(SHOULD_NOT_BE_EMPTY)));
        assertThat(validationOf(detailsWithEmptyCardNumber()), violates(on("cardNumber"), withError(SHOULD_NOT_BE_EMPTY)));
    }

    private CreditCardDetails detailsWithEmptyCardNumber() {
        return aVisa().withNumber("").build();
    }

    private CreditCardDetails detailsMissingCardNumber() {
        return aVisa().build();
    }
}
