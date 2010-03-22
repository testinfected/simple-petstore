package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemInventory;
import com.pyxis.petstore.domain.ItemNumber;
import com.pyxis.petstore.domain.Product;
import org.hamcrest.Matcher;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.builders.ItemBuilder;
import test.support.com.pyxis.petstore.db.Database;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.pyxis.matchers.persistence.HasFieldWithValue.hasField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class PersistentItemInventoryTest {

    Database database = Database.connect(get(SessionFactory.class));
    ItemInventory itemInventory = get(ItemInventory.class);
    Product product = aProduct().build();

    @Before
    public void cleanDatabase() {
        database.clean();
    }

    @After
    public void closeDatabase() {
        database.disconnect();
    }

    @Test public void
    findsItemsByNumber() throws Exception {
        havingPersisted(product);
        havingPersisted(anItem().of(product).withNumber("12345678"));

        Item found = itemInventory.find(new ItemNumber("12345678"));
        assertThat(found, hasProperty("number", equalTo("12345678")));
    }

    @Test public void
    findsItemsByProductNumber() throws Exception {
        Product product = aProduct().withNumber("LAB-1234").build();
        havingPersisted(product);
        havingPersisted(anItem().of(product), anItem().of(product));
        List<Item> itemsAvailable = itemInventory.findByProductNumber("LAB-1234");
        assertThat(itemsAvailable, everyItem(itemWithProduct(hasField("number", equalTo("LAB-1234")))));
    }

    @Test public void
    findsNothingIfProductHasNoItemInInventory() throws Exception {
        havingPersisted(aProduct().withNumber("DAL-5432"));

        List<Item> itemsAvailable = itemInventory.findByProductNumber("DAL-5432");
        assertTrue(itemsAvailable.isEmpty());
    }

    @Test public void
    itemIsInvalidWithoutAnItemNumber() throws Exception {
        Product product = aProduct().build();
        database.persist(product);
        assertFailsPersisting(anItemWithoutAReferenceNumber(product));
    }

    @Test public void
    referenceNumberShouldBeUnique() throws Exception {
        havingPersisted(product);
        ItemBuilder item = anItem().of(product).withNumber("LAB-1234");
        havingPersisted(item.build());
        try {
            database.persist(item.build());
            fail("Expected a ConstraintViolationException");
        } catch (org.hibernate.exception.ConstraintViolationException expected) {
            assertTrue(true);
        }
    }

    @Test(expected = ConstraintViolationException.class) public void
    itemIsInvalidWithoutAnAssociatedProduct() throws Exception {
        database.persist(anItemWithoutAnAssociatedProduct());
    }

    @Test(expected = ConstraintViolationException.class) public void
    itemIsInvalidWithoutAPrice() throws Exception {
        database.persist(anItemWithoutAPrice());
    }

    @Test public void
    canRoundTripItems() throws Exception {
        Product product = aProduct().build();
        final Collection<Item> items = Arrays.asList(
                anItem().of(product).withNumber("12345678").describedAs("Chocolate male").priced("58.00").build(),
                anItem().of(product).withNumber("87654321").build());

        database.persist(product);
        for (Item item : items) {
            database.persist(item);
            database.assertCanBeReloadedWithSameState(item);
        }
    }

    private void havingPersisted(Object... entities) throws Exception {
        database.persist(entities);
    }

    private void havingPersisted(Builder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private Matcher<Item> itemWithProduct(Matcher<? super Product> productMatcher) {
        return hasField("product", productMatcher);
    }

    private ItemBuilder anItemWithoutAReferenceNumber(Product product) {
        return anItem().of(product).withNumber(null);
    }

    private ItemBuilder anItemWithoutAnAssociatedProduct() {
        return anItem().of((Product) null);
    }

    private ItemBuilder anItemWithoutAPrice() {
        return anItem().priced((BigDecimal) null);
    }

    private void assertFailsPersisting(ItemBuilder item) throws Exception {
        try {
            database.persist(item);
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException expected) {
            assertTrue(true);
        }
    }
}
