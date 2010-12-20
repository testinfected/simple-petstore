package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ProductBuilder;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.an;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class BrowseCatalogFeature {
	
	PetStoreDriver petstore = new PetStoreDriver();
	DatabaseDriver database = new DatabaseDriver();

    Product iguana;

    @Before public void
    startApplication() throws Exception {
        database.start();
        petstore.start();
        given(aProduct().withName("Iguana"));
	}

    @After public void
    stopApplication() throws Exception {
        petstore.stop();
        database.stop();
    }

    private void given(final ProductBuilder product) throws Exception {
        this.iguana = product.build();
        database.given(this.iguana);
    }

    @Test public void
    consultsAProductCurrentlyOutOfStock() throws Exception {
        petstore.searchFor("Iguana");
        petstore.browseItemsOf("Iguana");
        petstore.showsNoItemAvailable();
    }

	@Test public void
    consultsAProductAvailableItems() throws Exception {
        Item greenAdult = an(iguana)
                .withNumber("12345678")
                .describedAs("Green Adult")
                .priced("18.50").build();
        database.given(greenAdult);

		petstore.searchFor("Iguana");
		petstore.browseItemsOf("Iguana");
		petstore.displaysItem("12345678", "Green Adult", "18.50");
	}
}
