package test.system.com.pyxis.petstore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.pyxis.petstore.domain.Item;
import org.junit.*;

import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.SearchResultsPage;
import test.system.com.pyxis.petstore.support.DatabaseSeeder;
import test.system.com.pyxis.petstore.support.PetStoreDriver;

import static test.system.com.pyxis.petstore.support.PetStoreContext.sessionFactory;

public class SearchFeature {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private final PetStoreDriver petstore = new PetStoreDriver();
    private HomePage home;

    @BeforeClass
    public static void seedDatabase() throws Exception {
        Item labrador = new Item("Labrador");
        labrador.setId(1L);
        new DatabaseSeeder(sessionFactory()).seed(labrador);
    }

    @Before
    public void setUp() throws Exception {
        home = petstore.start();
    }

    @Test
    public void displaysAnEmptyProductListWhenNoProductMatchesKeyword() throws Exception {
        SearchResultsPage resultsPage = home.searchFor("Squirrel");
        resultsPage.displays(NO_RESULT);
    }

    @Test
    public void displaysAListOfItemsWithNameMatchingQuery() throws Exception {
        SearchResultsPage resultsPage = home.searchFor("Labrador");
        resultsPage.displays(listWithItem("Labrador"));
    }

    @After
    public void tearDown() {
        petstore.close();
    }

    private static List<String> listWithItem(String... itemNames) {
        return Arrays.asList(itemNames);
    }

}
