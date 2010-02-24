package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemCatalog;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.Application;
import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.matchers.HasFieldWithValue;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.matchers.IsIterableWithSize;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

public class PersistentItemCatalogTest {

    ItemCatalog itemCatalog = Application.itemCatalog();
    Database database = Database.connect(Application.sessionFactory());

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
                and(anItem().withName("Dalmatian")),
                and(anItem().withName("Labrador"))
        );

        Collection<Item> matches = itemCatalog.findItemsByKeyword("Dalmatian");
        assertThat(matches, hasSize(equalTo(2)));
        assertThat(matches, containsItems(itemNamed("Dalmatian"), itemNamed("Dalmatian")));
    }

    private void havingPersisted(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private EntityBuilder<?> and(EntityBuilder<?> builder) {
        return builder;
    }

    private Matcher<Iterable<? super Item>> hasSize(final Matcher<? super Integer> sizeMatcher) {
        return IsIterableWithSize.withSize(sizeMatcher);
    }

    private Matcher<Iterable<Item>> containsItems(final Matcher<Item>... itemMatchers) {
        return Matchers.containsInAnyOrder(itemMatchers);
    }

    private Matcher<Item> itemNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }
}
