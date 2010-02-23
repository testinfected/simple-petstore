package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemCatalog;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.integration.com.pyxis.petstore.persistence.support.*;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static test.integration.com.pyxis.petstore.persistence.support.ItemBuilder.anItem;

public class PersistentItemCatalogTest {

    ItemCatalog itemCatalog = PetStoreContext.itemRepository();
    Database database = Database.connect(PetStoreContext.sessionFactory());

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

        Collection<Item> matches = itemCatalog.findItemsByKeyword("Dalmatian");
        assertThat(matches, isOfSize(1));
        assertThat(matches, contains(itemNamed("Dalmatian")));
    }

    private void havingPersisted(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private Matcher<Iterable<Item>> isOfSize(final int size) {
        return IsIterableWithSize.iterableWithSize(equalTo(size));
    }

    public Matcher<Iterable<? super Item>> contains(Matcher<Item> elementMatcher) {
      return IsCollectionContaining.hasItem(elementMatcher);
    }

    private Matcher<Item> itemNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }

    private EntityBuilder<?> and(EntityBuilder<?> builder) {
        return builder;
    }
}
