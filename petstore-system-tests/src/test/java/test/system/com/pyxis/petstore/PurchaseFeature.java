package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public class PurchaseFeature {

    SystemTestContext context = systemTesting();

    ServerDriver server = context.startServer();
    WebDriver browser = context.startBrowser();
    PetStoreDriver petstore = new PetStoreDriver(browser);

    @Before public void
    labradorsAreForSale() {
        Product labrador = aProduct().withName("Labrador Retriever").build();
        Product golden = aProduct().withName("Golden Retriever").build();
        context.given(labrador, golden);
        context.given(
                a(labrador).withNumber("11111111").describedAs("Male Adult").priced("599.00"),
                a(golden).withNumber("22222222").describedAs("Female Adult").priced("649.00"));
    }

    @Before public void
    startApplication() {
        petstore.open(context.routing());
    }

    @After public void
    stopApplication() {
        petstore.close();
        context.stopServer(server);
        context.stopBrowser(browser);
        context.cleanUp();
    }

    @Test public void
    purchasesSeveralItemsUsingACreditCard() {
        petstore.buy("Labrador Retriever", "11111111");
        petstore.buy("Golden Retriever", "22222222");
        petstore.checkout();
        petstore.showsTotalToPay("1248.00");

        petstore.pay("John", "Leclair", "jleclair@gmail.com", "Visa", "9999 9999 9999 9999", "12/12");
        petstore.showsTotalPaid("1248.00");
        petstore.showsLineItem("11111111", "Male Adult", "599.00");
        petstore.showsLineItem("22222222", "Female Adult", "649.00");
        petstore.showsBillingInformation("John", "Leclair", "jleclair@gmail.com");
        petstore.showsCreditCardDetails("Visa", "9999 9999 9999 9999", "12/12");

        petstore.showsCartIsEmpty();
    }
}
