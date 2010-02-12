package system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import system.com.pyxis.petstore.page.SearchPage;
import system.com.pyxis.petstore.page.SearchResultsPage;
import system.com.pyxis.petstore.support.PetStoreDriver;

import java.util.Collections;
import java.util.List;

public class SearchFeature {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private PetStoreDriver petstore = new PetStoreDriver();

    @Before
	public void setUp()
	{
	}

	@Test
	public void displaysAnEmptyProductListWhenNoProductMatchesKeyword() throws Exception {
        SearchPage searchPage = petstore.navigateTo(SearchPage.class);
		SearchResultsPage resultsPage = searchPage.search("Squirrel");
		resultsPage.displays(NO_RESULT);
	}

	/*
	@Test
	public void displaysAListOfItemsWithNameMatchingQuery() throws Exception {
		SearchPage searchPage = petstore.navigateTo(SearchPage.class);
		SearchResultsPage resultsPage = searchPage.search("Dalmatian");
		resultsPage.displays(listWithItem("Dalmatian"));
	}
	*/

    @After
    public void tearDown() {
        petstore.dispose();
    }
}
