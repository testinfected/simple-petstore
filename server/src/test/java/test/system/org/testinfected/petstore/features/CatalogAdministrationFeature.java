package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.Actors;
import test.support.org.testinfected.petstore.web.actors.Administrator;
import test.support.org.testinfected.petstore.web.actors.Customer;
import test.support.org.testinfected.petstore.web.drivers.ServerDriver;

import java.io.IOException;

public class CatalogAdministrationFeature {

    ServerDriver server = new ServerDriver();
    Actors actors = new Actors();
    Administrator administrator = actors.administrator();
    Customer customer = actors.customer();

    @Before public void
    startServer() throws Exception {
        server.start();
    }

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @After public void
    stopUsingApplication() {
        customer.done();
    }

    @Test public void
    addingAProductToTheStore() throws IOException {
        administrator.addProductToCatalog("LIZ-0001", "Iguana", "Big lizard", "iguana.png");

        customer.browseCatalog()
                .lookUpProductByName("Iguana")
                .seesAvailableProduct("LIZ-0001");

        customer.browseCatalog()
                .checkAvailabilityOfProduct("Iguana")
                .seesNoItemAvailable();
    }

    @Test public void
    addingItemsToInventory() throws IOException {
        administrator.addProductToCatalog("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        administrator.addItemToInventory("LIZ-0001", "12345678", "Green Adult", "18.50");
        administrator.addItemToInventory("LIZ-0001", "87654321", "Blue Youngster", "28.50");

        customer.browseCatalog()
                .checkAvailabilityOfProduct("Iguana")
                .seesAvailableItem("12345678", "Green Adult", "18.50")
                .seesAvailableItem("87654321", "Blue Youngster", "28.50");
    }
}
