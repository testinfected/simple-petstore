package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemInventory;
import com.pyxis.petstore.domain.Product;
import org.hamcrest.Matcher;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ItemBuilder;
import test.support.com.pyxis.petstore.db.Database;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.pyxis.matchers.persistence.HasFieldWithValue.hasField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class PersistentItemInventoryTest {

    Database database = Database.connect(get(SessionFactory.class));
    ItemInventory itemInventory = get(ItemInventory.class);

    @Before
    public void cleanDatabase() {
        database.clean();
    }

    @After
    public void closeDatabase() {
        database.disconnect();
    }

    @Test public void
    canFindItemsByProductNumber() throws Exception {
        Product product = aProduct().withNumber("LAB-1234").build();
        database.persist(product);
        database.persist(anItem().of(product), anItem().of(product));
        List<Item> itemsFound = itemInventory.findByProductNumber("LAB-1234");
        assertThat(itemsFound, everyItem(itemWithProduct(hasField("number", equalTo("LAB-1234")))));
    }

    @Test public void
    itemIsInvalidWithoutAnItemNumber() throws Exception {
        Product product = aProduct().build();
        database.persist(product);
        assertFailsPersisting(anItemWithoutAReferenceNumber(product));
    }

    private ItemBuilder anItemWithoutAReferenceNumber(Product product) {
        return anItem().of(product).withNumber(null);
    }

    private void assertFailsPersisting(ItemBuilder item) throws Exception {
        try {
            database.persist(item);
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException expected) {
            assertTrue(true);
        }
    }

    @Test(expected = ConstraintViolationException.class) public void
    itemIsInvalidWithoutAnAssociatedProduct() throws Exception {
        database.persist(anItemWithoutAnAssociatedProduct());
    }

    private ItemBuilder anItemWithoutAnAssociatedProduct() {
        return anItem().of((Product) null);
    }

    private Matcher<Item> itemWithProduct(Matcher<? super Product> productMatcher) {
        return hasField("product", productMatcher);
    }
}
