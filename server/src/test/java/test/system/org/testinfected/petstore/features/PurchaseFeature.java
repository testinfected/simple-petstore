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

public class PurchaseFeature {

    ServerDriver server = new ServerDriver();
    Actors actors = new Actors();
    Administrator administrator = actors.administrator();
    Customer customer = actors.customer();

    String labradorPrice = "599.00";
    String goldenPrice = "649.00";
    String totalPrice = "1248.00";

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
    purchasingSeveralItemsUsingACreditCard() throws Exception {
        havingRetrieversInStock();

        customer.startShopping()
                .addToCart("Labrador Retriever", "11111111")
                .addToCart("Golden Retriever", "22222222")
                .seesTotalToPay(totalPrice)
                .completeOrder("John", "Doe", "jdoe@gmail.com", "Visa", "4111111111111111", "12/12")
                .seesTotalPaid("1248.00")
                .seesOrderedItems(
                    item("11111111", "Male Adult", "599.00"), item("22222222", "Female Adult", "649.00"))
                .seesBillingInformation("John", "Doe", "jdoe@gmail.com")
                .seesCreditCardDetails("Visa", "4111111111111111", "12/12");

        customer.seesCartIsEmpty();
    }

    private void havingRetrieversInStock() throws IOException {
        administrator.addProductToCatalog("DOG-0001", "Labrador Retriever", "Friendly dog", "labrador.jpg");
        administrator.addProductToCatalog("DOG-0002", "Golden Retriever", "Joyful dog", "golden.jpg");
        administrator.addItemToInventory("DOG-0001", "11111111", "Male Adult", labradorPrice);
        administrator.addItemToInventory("DOG-0002", "22222222", "Female Adult", goldenPrice);
    }
}