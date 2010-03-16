package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.ItemNumber;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ItemNumberFaker;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class ItemNumberTest {

    String SHOULD_NOT_BE_NULL = "{javax.validation.constraints.NotNull.message}";

    @Test public void
    isValidWithANonNullNumber() {
        ItemNumber aValidItemNumber = ItemNumberFaker.aNumber();
        assertThat(validationOf(aValidItemNumber), succeeds());
    }

    @Test public void
    isInvalidWithANullNumber() {
        ItemNumber anItemNumberWithoutANumber = new ItemNumber(null);
        assertThat(validationOf(anItemNumberWithoutANumber), violates(on("number"), withError(SHOULD_NOT_BE_NULL)));
    }
}
