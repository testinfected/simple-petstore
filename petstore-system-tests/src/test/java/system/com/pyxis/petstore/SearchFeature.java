package system.com.pyxis.petstore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import system.com.pyxis.petstore.page.SearchPage;
import system.com.pyxis.petstore.page.SearchResultsPage;
import system.com.pyxis.petstore.support.PetStoreDriver;

import com.pyxis.petstore.domain.Item;

public class SearchFeature {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private static PetStoreDriver petstore = new PetStoreDriver();

    @BeforeClass
	public static void setUp()
	{
    	Item dalmatian = new Item("Dalmatian");
    	dalmatian.setId(1L);
		petstore.addToInventory(dalmatian);
	}

	@Test
	public void displaysAnEmptyProductListWhenNoProductMatchesKeyword() throws Exception {
        SearchPage searchPage = petstore.navigateTo(SearchPage.class);
		SearchResultsPage resultsPage = searchPage.search("Squirrel");
		resultsPage.displays(NO_RESULT);
	}

	@Test
	public void displaysAListOfItemsWithNameMatchingQuery() throws Exception {
		SearchPage searchPage = petstore.navigateTo(SearchPage.class);
		SearchResultsPage resultsPage = searchPage.search("Dalmatian");
		resultsPage.displays(listWithItem("Dalmatian"));
	}
				
	@AfterClass
    public static void tearDown() {
        petstore.dispose();
    }

	private List<String> listWithItem(String... itemNames) {
		return Arrays.asList(itemNames);
	}
	
}
