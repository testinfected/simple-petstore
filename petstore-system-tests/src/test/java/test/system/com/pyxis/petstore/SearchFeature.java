package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.integration.com.pyxis.petstore.persistence.support.Database;
import test.integration.com.pyxis.petstore.persistence.support.EntityBuilder;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.SearchResultsPage;
import test.system.com.pyxis.petstore.support.PetStoreDriver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static test.integration.com.pyxis.petstore.persistence.support.ItemBuilder.anItem;
import static test.system.com.pyxis.petstore.support.PetStoreContext.sessionFactory;

public class SearchFeature {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private PetStoreDriver petstore = new PetStoreDriver();
    private Database database = new Database(sessionFactory()).connect();
    private HomePage home;

    @Before
    public void setUp() throws Exception {
        home = petstore.start();
    }

    @Test
    public void displaysAnEmptyProductListWhenNoProductNameMatches() throws Exception {
        SearchResultsPage resultsPage = home.searchFor("Squirrel");
        resultsPage.displays(NO_RESULT);
    }

    @Test
    public void displaysAListOfItemsWhoseNamesMatch() throws Exception {
        given(anItem().withName("Labrador"));
        SearchResultsPage resultsPage = home.searchFor("Labrador");
        resultsPage.displays(listWithItem("Labrador"));
    }

    @After
    public void tearDown() {
        petstore.close();
    }

    private void given(EntityBuilder... builders) throws Exception {
        database.persist(builders);
    }

    private static List<String> listWithItem(String... itemNames) {
        return Arrays.asList(itemNames);
    }

}
