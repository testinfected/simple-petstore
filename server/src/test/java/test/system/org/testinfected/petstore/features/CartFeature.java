package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.Actors;
import test.support.org.testinfected.petstore.web.actors.Administrator;
import test.support.org.testinfected.petstore.web.actors.Customer;
import test.support.org.testinfected.petstore.web.drivers.ServerDriver;

import java.io.IOException;

import static test.system.org.testinfected.petstore.features.Item.item;

public class CartFeature {

    ServerDriver server = new ServerDriver();
    Actors actors = new Actors();
    Administrator administrator = actors.administrator();
    Customer customer = actors.customer();

    @Before
    public void
    startServer() throws Exception {
        server.start();
    }

    @After
    public void
    stopServer() throws Exception {
        server.stop();
    }

    @After public void
    stopUsingApplication() {
        customer.done();
    }

    @Test
    public void
    shoppingForItemsAndAddingThemToOurCart() throws IOException {
        administrator.addProductToCatalog("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        administrator.addItemToInventory("LIZ-0001", "12345678", "Green Adult", "18.50");
        administrator.addItemToInventory("LIZ-0001", "87654321", "Blue Female", "58.97");

        customer.loginAs("joe")
                .seesCartIsEmpty();

        customer.startShopping()
                .addToCart("Iguana", "12345678")
                .seesCartContent("18.50", item("12345678", "Green Adult", "18.50"));
        customer.seesCartTotalQuantity(1);

        customer.continueShopping()
                .addToCart("Iguana", "87654321")
                .seesCartContent("77.47",
                        item("12345678", "Green Adult", "18.50"),
                        item("87654321", "Blue Female", "58.97"));
        customer.seesCartTotalQuantity(2);

        customer.continueShopping()
                .addToCart("Iguana", "12345678")
                .seesCartContent("95.97",
                        item("12345678", "Green Adult", "18.50", 2, "37.00"),
                        item("87654321", "Blue Female", "58.97"));
        customer.seesCartTotalQuantity(3);
    }
}
