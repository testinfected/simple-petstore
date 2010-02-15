package system.com.pyxis.petstore;

import com.pyxis.petstore.domain.Item;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import system.com.pyxis.petstore.page.HomePage;
import system.com.pyxis.petstore.page.SearchPage;
import system.com.pyxis.petstore.page.SearchResultsPage;
import system.com.pyxis.petstore.support.DatabaseSeeder;
import system.com.pyxis.petstore.support.PetStoreDriver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static system.com.pyxis.petstore.support.PetStoreContext.sessionFactory;

public class SearchFeature {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private static final PetStoreDriver petstore = new PetStoreDriver();
    private HomePage home;

    @BeforeClass
    public static void seedDatabase() {
        Item dalmatian = new Item("Dalmatian");
        dalmatian.setId(1L);
        new DatabaseSeeder(sessionFactory()).store(dalmatian);
    }

    @Before
    public void setUp() throws Exception {
        home = petstore.start();
    }

    @Test
    public void displaysAnEmptyProductListWhenNoProductMatchesKeyword() throws Exception {
        SearchPage searchPage = petstore.navigateTo(SearchPage.class);
        SearchResultsPage resultsPage = searchPage.searchFor("Squirrel");
        resultsPage.displays(NO_RESULT);
    }

    @Test
    public void displaysAListOfItemsWithNameMatchingQuery() throws Exception {
        SearchPage searchPage = petstore.navigateTo(SearchPage.class);
        SearchResultsPage resultsPage = searchPage.searchFor("Dalmatian");
        resultsPage.displays(listWithItem("Dalmatian"));
    }

    @AfterClass
    public static void tearDown() {
        petstore.dispose();
    }

    private static List<String> listWithItem(String... itemNames) {
        return Arrays.asList(itemNames);
    }

}
