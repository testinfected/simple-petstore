package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public class ShopFeature {

    SystemTestContext context = systemTesting();

    ServerDriver server = context.startServer();
    WebDriver browser = context.startBrowser();
    PetStoreDriver petstore = new PetStoreDriver(browser);

    @Before public void
    startApplication() {
        petstore.open(context.routes());
    }

    @After public void
    stopApplication() {
        petstore.close();
        context.stopServer(server);
        context.stopBrowser(browser);
        context.cleanUp();
    }

    @Before public void
    iguanaAreForSale() {
        Product iguana = aProduct().withName("Iguana").build();
        context.given(iguana);
        context.given(
                anItem().of(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"),
                anItem().of(iguana).withNumber("87654321").describedAs("Blue Female").priced("58.97"));
    }

    @Test public void
    shopsForItemsAndAddsThemToCart() {
        petstore.showsCartIsEmpty();

        petstore.buy("Iguana", "12345678");
        petstore.showsItemInCart("12345678", "Green Adult", "18.50");
        petstore.showsGrandTotal("18.50");
        petstore.showsCartTotalQuantity(1);

        petstore.buy("Iguana", "87654321");
        petstore.showsItemInCart("87654321", "Blue Female", "58.97");
        petstore.showsGrandTotal("77.47");
        petstore.showsCartTotalQuantity(2);
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() {
        petstore.buy("Iguana", "12345678");
        petstore.showsItemQuantity("12345678", 1);

        petstore.buy("Iguana", "12345678");
        petstore.showsItemQuantity("12345678", 2);
        petstore.showsCartTotalQuantity(2);
    }
}