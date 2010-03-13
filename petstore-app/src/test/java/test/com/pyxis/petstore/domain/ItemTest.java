package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.Product;
import org.junit.Test;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class ItemTest {

    @Test public void
    isInvalidWithoutAProduct() {
        Item anItemWithNoAssociatedProduct = anItem().of((Product) null).build();
        assertThat(validationOf(anItemWithNoAssociatedProduct), violates(on("product"), withError("NotNull")));
    }

    @Test public void
    isInvalidWithoutANumber() {
        Item anItemWithoutANumber = anItem().with(null).build();
        assertThat(validationOf(anItemWithoutANumber), violates(on("referenceNumber"), withError("NotNull")));
    }

    @Test public void
    isInvalidWithoutAValidNumber() {
        Item anItemWithAnInvalidNumber = anItem().withNumber(null).build();
        assertThat(validationOf(anItemWithAnInvalidNumber), violates(on("referenceNumber.number"), withError("NotNull")));
    }

    @Test public void
    isValidWithAValidNumberAndAnAssociatedProduct() {
        Item aValidItem = anItem().build();
        assertThat(validationOf(aValidItem), succeeds());
    }
}
