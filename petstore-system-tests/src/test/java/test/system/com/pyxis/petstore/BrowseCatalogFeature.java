package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pyxis.petstore.domain.Item;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ItemsPage;
import test.system.com.pyxis.petstore.page.ProductsPage;

public class BrowseCatalogFeature {
	
	PetStoreDriver petstore = new PetStoreDriver();
	DatabaseDriver database = new DatabaseDriver();
	private HomePage homePage;
	
	@Before
	public void setUp() throws Exception {
		homePage = petstore.start();
		database.open();
	}

	@Test
	public void listsItemsOfProductsAvailableInCatalog() throws Exception {
		Item aGreenIguana = null;// = anItem("1234").describedAs("Green Adult").priced(18.5);
		database.given(aProduct().withName("Iguana").with(aGreenIguana));
		ProductsPage productsPage = homePage.searchFor("Iguana");
		ItemsPage itemsPage = productsPage.browseItemsOf("Iguana");
		itemsPage.displaysItem("1234", "Green Adult", 18.5f);
	}
	
	@After
	public void tearDown() {
		petstore.close();
		database.close();
	}
	
}
