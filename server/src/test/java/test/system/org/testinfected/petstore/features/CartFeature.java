package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.ApplicationDriver;
import test.support.org.testinfected.petstore.web.TestEnvironment;

import java.io.IOException;

import static test.system.org.testinfected.petstore.features.Item.item;

public class CartFeature {

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
    shoppingForItemsAndAddingThemToOurCart() throws IOException {
        application.havingProductInCatalog("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        application.havingItemInStore("LIZ-0001", "12345678", "Green Adult", "18.50");
        application.havingItemInStore("LIZ-0001", "87654321", "Blue Female", "58.97");
        application.showsCartIsEmpty();

        application.buyItem("Iguana", "12345678");
        application.showsCartTotalQuantity(1);
        application.showsCartContent("18.50", item("12345678", "Green Adult", "18.50"));

        application.buyItem("Iguana", "87654321");
        application.showsCartTotalQuantity(2);
        application.showsCartContent("77.47",
                item("12345678", "Green Adult", "18.50"),
                item("87654321", "Blue Female", "58.97"));

        application.buyItem("Iguana", "12345678");
        application.showsCartTotalQuantity(3);
        application.showsCartContent("95.97",
                item("12345678", "Green Adult", "18.50", 2, "37.00"),
                item("87654321", "Blue Female", "58.97"));
    }
}
