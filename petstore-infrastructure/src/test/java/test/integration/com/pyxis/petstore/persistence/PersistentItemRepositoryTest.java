package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;
import com.pyxis.petstore.domain.Product;
import org.hamcrest.Matcher;
import org.hibernate.PropertyValueException;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.db.Database;

import java.util.List;

import static com.pyxis.matchers.persistence.HasFieldWithValue.hasField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.junit.Assert.fail;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class PersistentItemRepositoryTest {

    Database database = Database.connect(get(SessionFactory.class));
    ItemRepository itemRepository = get(ItemRepository.class);

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
        List<Item> itemsFound = itemRepository.findByProductNumber("LAB-1234");
        assertThat(itemsFound, everyItem(itemWithProduct(hasField("number", equalTo("LAB-1234")))));
    }

    @Test public void
    itemIsInvalidWithoutAnItemNumber() throws Exception {
        Product product = aProduct().build();
        database.persist(product);

        try {
            database.persist(anItem().of(product).withNumber(null));
            fail("Expected PropertyValueException");
        } catch (PropertyValueException expected) {
        }
    }

    @Test(expected = PropertyValueException.class) public void
    itemIsInvalidWithoutAnAssociatedProduct() throws Exception {
        database.persist(anItem().of((Product) null));
    }

    private Matcher<Item> itemWithProduct(Matcher<? super Product> productMatcher) {
        return hasField("product", productMatcher);
    }
}
