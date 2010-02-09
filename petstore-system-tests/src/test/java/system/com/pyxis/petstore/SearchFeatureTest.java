package system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SearchFeatureTest {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private PetStoreDriver petstore = new PetStoreDriver();

    @Before
	public void setUp()
	{
	}

	@Test
	public void displaysAnEmptyProductListWhenNoProductMatchesKeyword() throws Exception {
        SearchPage searchPage = petstore.navigateTo(SearchPage.class);
//        SearchResultsPage resultsPage = searchPage.search("PetStore");
//        resultsPage.displays(NO_RESULT);
	}

    @After
    public void tearDown() {
        petstore.dispose();
    }
}
