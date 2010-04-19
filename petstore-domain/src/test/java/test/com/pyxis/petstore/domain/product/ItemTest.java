package test.com.pyxis.petstore.domain.product;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ItemBuilder;

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
        assertThat(validationOf(anItemWithoutAProduct()), violates(on("product"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isInvalidWithoutANumber() {
        assertThat(validationOf(anItemWithoutANumber()), violates(on("number"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isInvalidWithoutAValidNumber() {
        assertThat(validationOf(anItemWithAnInvalidNumber()), violates(on("number.number"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isInvalidWithoutAPrice() {
        assertThat(validationOf(anItemWithoutAPrice()), violates(on("price"), withError(SHOULD_NOT_BE_NULL)));
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

    private ItemBuilder anItemWithoutAProduct() {
        return anItem().of((Product) null);
    }

    private ItemBuilder anItemWithoutANumber() {
        return anItem().with(null);
    }

    private ItemBuilder anItemWithAnInvalidNumber() {
        return anItem().withNumber(null);
    }

    private ItemBuilder anItemWithoutAPrice() {
        return anItem().priced((BigDecimal) null);
    }
}
