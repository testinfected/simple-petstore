package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.Builder;
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
        petstore.start();
    }

    @Before
    public void storeSellsIguana() throws Exception {
        database.start();
        iguana = given(aProduct().withName("Iguana"));
    }

    @After public void
    stopApplication() throws Exception {
        petstore.stop();
        database.stop();
    }

    private <T> T given(Builder<T> builder) throws Exception {
        return database.contain(builder);
    }

    @Test public void
    consultsAProductCurrentlyOutOfStock() throws Exception {
        petstore.consultInventoryOf("Iguana");
        petstore.showsNoItemAvailable();
    }

    @Test public void
    consultsAProductAvailableItems() throws Exception {
        given(an(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"));

        petstore.consultInventoryOf("Iguana");
        petstore.displaysItem("12345678", "Green Adult", "18.50");
    }
}
