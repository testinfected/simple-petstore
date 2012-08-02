package test.system.com.pyxis.petstore.old;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.OldSystemTestContext;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.an;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.OldSystemTestContext.systemTesting;

public class BrowseCatalogFeature {

    OldSystemTestContext context = systemTesting();

    ApplicationDriver petstore;
    Product iguana;

    @Before public void
    startApplication() {
        petstore = context.startApplication();
        iguanaAreForSale();
    }

    @After public void
    stopApplication() {
        context.stopApplication(petstore);
    }

    private void iguanaAreForSale() {
        iguana = aProduct().named("Iguana").build();
        context.given(iguana);
    }

    @Test public void
    consultsAProductCurrentlyOutOfStock() {
        petstore.consultInventoryOf("Iguana");
        petstore.showsNoItemAvailable();
    }

    @Test public void
    consultsAProductAvailableItems() {
        context.given(an(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"));

        petstore.consultInventoryOf("Iguana");
        petstore.displaysItem("12345678", "Green Adult", "18.50");
        petstore.continueShopping();
    }
}
