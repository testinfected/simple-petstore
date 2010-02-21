package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemCatalog;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.integration.com.pyxis.petstore.persistence.support.Database;
import test.integration.com.pyxis.petstore.persistence.support.EntityBuilder;
import test.integration.com.pyxis.petstore.persistence.support.HasFieldWithValue;
import test.integration.com.pyxis.petstore.persistence.support.PetStoreContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static test.integration.com.pyxis.petstore.persistence.support.ItemBuilder.anItem;

public class PersistentItemCatalogTest {

    ItemCatalog itemCatalog = PetStoreContext.itemRepository();
    Database database = new Database(PetStoreContext.sessionFactory()).connect();

    @Before
    public void cleanDatabase() {
        database.clean();
    }

    @After
    public void closeDatabase() {
        database.disconnect();
    }

    @Test
    public void wontFindAnythingIfNoItemNameMatches() throws Exception {
        havingPersisted(anItem().withName("Dalmatian"));

        List<Item> matchingItems = itemCatalog.findItemsByKeyword("Squirrel");
        assertTrue(matchingItems.isEmpty());
    }

    @Test
    public void canFindItemsWithAGivenName() throws Exception {
        havingPersisted(
                anItem().withName("Dalmatian"),
                and(anItem().withName("Labrador"))
        );

        List<Item> matches = itemCatalog.findItemsByKeyword("Dalmatian");
        assertThat(matches.size(), is(equalTo(1)));
        assertThat(matches, hasItem(itemNamed("Dalmatian")));
    }

//    private <T> Matcher<? extends T> withSamePersistentFieldsAs(T entity) {
//        return SamePersistentFieldsAs.samePersistentFieldsAs(entity);
//    }

    private void havingPersisted(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private Matcher<Item> itemNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }

    private EntityBuilder<?> and(EntityBuilder<?> builder) {
        return builder;
    }
}
