package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.Product;
import org.junit.Test;

import java.math.BigDecimal;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class ItemTest {

    String SHOULD_NOT_BE_NULL = "{javax.validation.constraints.NotNull.message}";

    @Test public void
    isInvalidWithoutAProduct() {
        Item anItemWithNoAssociatedProduct = anItem().of((Product) null).build();
        assertThat(validationOf(anItemWithNoAssociatedProduct), violates(on("product"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isInvalidWithoutANumber() {
        Item anItemWithoutANumber = anItem().with(null).build();
        assertThat(validationOf(anItemWithoutANumber), violates(on("referenceNumber"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isInvalidWithoutAValidNumber() {
        Item anItemWithAnInvalidNumber = anItem().withNumber(null).build();
        assertThat(validationOf(anItemWithAnInvalidNumber), violates(on("referenceNumber.number"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isInvalidWithoutAPrice() {
        Item anItemWithoutAPrice = anItem().priced((BigDecimal) null).build();
        assertThat(validationOf(anItemWithoutAPrice), violates(on("price"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isValidWithAValidNumberAPriceAndAnAssociatedProduct() {
        Item aValidItem = anItem().withNumber("12345678").of(aProduct()).priced("100.00").build();
        assertThat(validationOf(aValidItem), succeeds());
    }

    @Test public void
    itemIsUniquelyIdentifiedByItsNumber() {
        Item item = anItem().withNumber("12345678").build();
        Item shouldMatch = anItem().withNumber("12345678").build();
        Item shouldNotMatch = anItem().withNumber("87654321").build();
        assertThat("items should match", item, equalTo(shouldMatch));
        assertThat("items hash codes should match", item.hashCode(), equalTo(shouldMatch.hashCode()));
        assertThat("items should not match", item, not(equalTo(shouldNotMatch)));
    }
}
