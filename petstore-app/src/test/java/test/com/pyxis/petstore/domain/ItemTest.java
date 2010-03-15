package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.Product;
import org.junit.Test;

import java.math.BigDecimal;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
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
    isInvalidWithoutAPrice() {
        Item anItemWithoutAPrice = anItem().priced((BigDecimal) null).build();
        assertThat(validationOf(anItemWithoutAPrice), violates(on("price"), withError("NotNull")));
    }

    @Test public void
    isValidWithAValidNumberAPriceAndAnAssociatedProduct() {
        Item aValidItem = anItem().withNumber("12345678").of(aProduct()).priced("100.00").build();
        assertThat(validationOf(aValidItem), succeeds());
    }
}
