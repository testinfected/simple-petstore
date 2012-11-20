package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.ApplicationDriver;
import test.support.org.testinfected.petstore.web.TestEnvironment;

import java.io.IOException;

public class ShopFeature {

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
    shopsForItemsAndAddsThemToCart() throws IOException {
        application.addProduct("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        application.addItem("LIZ-0001", "12345678", "Green Adult", "18.50");
        application.addItem("LIZ-0001", "87654321", "Blue Female", "58.97");

        application.showsCartIsEmpty();

        application.buy("Iguana", "12345678");
        application.showsCartTotalQuantity(1);
        application.showsGrandTotal("18.50");
        application.showsItemInCart("12345678", "Green Adult", "18.50");
        application.continueShopping();

        application.buy("Iguana", "87654321");
        application.showsCartTotalQuantity(2);
        application.showsGrandTotal("77.47");
        application.showsItemInCart("87654321", "Blue Female", "58.97");
    }


    @Test public void
    shopsForTheSameItemMultipleTimes() throws IOException {
        application.addProduct("LIZ-0001", "Iguana", "Big lizard", "iguana.png");
        application.addItem("LIZ-0001", "12345678", "Green Adult", "18.50");
        application.buy("Iguana", "12345678");

        application.showsItemQuantity("12345678", 1);
        application.continueShopping();

        application.buy("Iguana", "12345678");
        application.showsItemQuantity("12345678", 2);
        application.showsCartTotalQuantity(2);
    }
}
