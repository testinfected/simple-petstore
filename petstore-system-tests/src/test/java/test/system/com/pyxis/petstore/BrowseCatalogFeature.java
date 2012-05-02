package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.an;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public class BrowseCatalogFeature {

    SystemTestContext context = systemTesting();

    ServerDriver server = context.startServer();
    WebDriver browser = context.startBrowser();
    PetStoreDriver petstore = new PetStoreDriver(browser);

    Product iguana;

    @Before public void
    startApplication() {
        petstore.open(context.routes());
    }

    @After public void
    stopApplication() {
        petstore.close();
        context.stopBrowser(browser);
        context.stopServer(server);
        context.cleanUp();
    }

    @Before public void
    iguanaAreForSale() {
        iguana = aProduct().withName("Iguana").build();
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
    }
}
