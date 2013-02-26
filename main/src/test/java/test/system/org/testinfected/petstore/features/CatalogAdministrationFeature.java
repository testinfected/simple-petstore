package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.ApplicationDriver;
import test.support.org.testinfected.petstore.web.TestEnvironment;

import java.io.IOException;

public class CatalogAdministrationFeature {

    ApplicationDriver application = new ApplicationDriver(TestEnvironment.load());

    @Before public void
    startApplication() throws Exception {
        application.start();
    }

    @After public void
    stopApplication() throws Exception {
        application.stop();
    }

    @Test public void
    addingAProductToTheStore() throws IOException {
        application.addProductToCatalog("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        application.showsProductInCatalog("LIZ-0001", "Iguana");
        application.showsNoItemAvailableFor("Iguana");
    }

    @Test public void
    addingItemsToInventory() throws IOException {
        application.addProductToCatalog("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        application.addItemToInventory("LIZ-0001", "12345678", "Green Adult", "18.50");
        application.addItemToInventory("LIZ-0001", "87654321", "Blue Youngster", "28.50");

        application.showsAvailableItem("Iguana", "12345678", "Green Adult", "18.50");
        application.showsAvailableItem("Iguana", "87654321", "Blue Youngster", "28.50");
    }
}
