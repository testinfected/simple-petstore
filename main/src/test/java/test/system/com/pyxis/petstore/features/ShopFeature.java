package test.system.com.pyxis.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.TestEnvironment;

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

    @Ignore("wip") @Test public void
    shopsForItemsAndAddsThemToCart() throws IOException {
        application.addProduct("LIZ-001", "Iguana");
        application.addItem("LIZ-001", "12345678", "Green Adult", "18.50");
        application.addItem("LIZ-001", "87654321", "Blue Female", "58.97");

        application.showsCartIsEmpty();

        application.buy("Iguana", "12345678");
        application.showsItemInCart("12345678", "Green Adult", "18.50");
        application.showsGrandTotal("18.50");
        application.showsCartTotalQuantity(1);
        application.continueShopping();

        application.buy("Iguana", "87654321");
        application.showsItemInCart("87654321", "Blue Female", "58.97");
        application.showsGrandTotal("77.47");
        application.showsCartTotalQuantity(2);
    }
}
