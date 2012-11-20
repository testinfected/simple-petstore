package test.unit.org.testinfected.petstore.product;

import org.testinfected.petstore.product.Item;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class ItemTest {

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
