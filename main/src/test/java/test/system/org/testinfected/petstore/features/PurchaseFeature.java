package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.ApplicationDriver;
import test.support.org.testinfected.petstore.web.TestEnvironment;

import java.io.IOException;

import static test.system.org.testinfected.petstore.features.Item.item;

public class PurchaseFeature {

    ApplicationDriver application = new ApplicationDriver(TestEnvironment.load());

    String labradorPrice = "599.00";
    String goldenPrice = "649.00";
    String totalPrice = "1248.00";

    @Before public void
    startApplication() throws Exception {
        application.start();
    }

    @After public void
    stopApplication() throws Exception {
        application.stop();
    }

    @Test public void
    purchasingSeveralItemsUsingACreditCard() throws Exception {
        havingRetrieversInStock();

        application.buyItem("Labrador Retriever", "11111111");
        application.buyItem("Golden Retriever", "22222222");
        application.showsTotalToPay(totalPrice);

        application.pay("John", "Doe", "jdoe@gmail.com", "Visa", "4111111111111111", "12/12");
        application.showsCartIsEmpty();
        application.showsOrderTotal("1248.00");
        application.showsOrderedItems(
                item("11111111", "Male Adult", "599.00"), item("22222222", "Female Adult", "649.00"));
        application.showsBillingInformation("John", "Doe", "jdoe@gmail.com");
        application.showsCreditCardDetails("Visa", "4111111111111111", "12/12");
    }

    private void havingRetrieversInStock() throws IOException {
        application.havingProductInCatalog("DOG-0001", "Labrador Retriever", "Friendly dog", "labrador.jpg");
        application.havingProductInCatalog("DOG-0002", "Golden Retriever", "Joyful dog", "golden.jpg");
        application.havingItemInStore("DOG-0001", "11111111", "Male Adult", labradorPrice);
        application.havingItemInStore("DOG-0002", "22222222", "Female Adult", goldenPrice);
    }
}
