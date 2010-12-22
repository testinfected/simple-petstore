package test.com.pyxis.petstore.domain.product;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.junit.Test;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class ItemTest {

    String SHOULD_NOT_BE_NULL = "{javax.validation.constraints.NotNull.message}";
    ItemNumber AN_INVALID_NUMBER = new ItemNumber(null);

    @SuppressWarnings("unchecked")
    @Test public void
    isInvalidWithoutAProduct() {
        assertThat("constraint violations", validationOf(anItem().withoutAProduct()), violates(on("product"), withError(SHOULD_NOT_BE_NULL)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    isInvalidWithoutANumber() {
        assertThat("constraint violations", validationOf(anItem().withoutANumber()), violates(on("number"), withError(SHOULD_NOT_BE_NULL)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    isInvalidWithoutAValidNumber() {
        assertThat("constraint violations", validationOf(anItem().with(AN_INVALID_NUMBER)), violates(on("number.number"), withError(SHOULD_NOT_BE_NULL)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    isInvalidWithoutAPrice() {
        assertThat("constraint violations", validationOf(anItem().withoutAPrice()), violates(on("price"), withError(SHOULD_NOT_BE_NULL)));
    }

    @Test public void
    isValidWithAValidNumberAPriceAndAnAssociatedProduct() {
        Item aValidItem = anItem().withNumber("12345678").of(aProduct()).priced("100.00").build();
        assertThat("constraint violations", validationOf(aValidItem), succeeds());
    }

    @Test public void
    itemIsUniquelyIdentifiedByItsNumber() {
        Item item = anItem().withNumber("12345678").build();
        Item shouldMatch = anItem().withNumber("12345678").build();
        Item shouldNotMatch = anItem().withNumber("87654321").build();
        assertThat("item", item, equalTo(shouldMatch));
        assertThat("hash code", item.hashCode(), equalTo(shouldMatch.hashCode()));
        assertThat("item", item, not(equalTo(shouldNotMatch)));
    }
}
