package test.com.pyxis.petstore.domain.product;

import com.pyxis.petstore.domain.product.ItemNumber;
import org.junit.Test;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.ItemNumberFaker.aNumber;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class ItemNumberTest {

    String SHOULD_NOT_BE_NULL = "{javax.validation.constraints.NotNull.message}";

    @Test public void
    isValidWithANumber() {
        ItemNumber aValidItemNumber = aNumber();
        assertThat("constraint violations", validationOf(aValidItemNumber), succeeds());
    }

    @Test public void
    isInvalidWithoutANumber() {
        ItemNumber anEmptyItemNumber = new ItemNumber(null);
        assertThat("constraint violations", validationOf(anEmptyItemNumber), violates(on("number"), withError(SHOULD_NOT_BE_NULL)));
    }
}
