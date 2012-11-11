package test.system.com.pyxis.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.TestEnvironment;

public class PurchaseFeature {

    ApplicationDriver application = new ApplicationDriver(TestEnvironment.load());

    String labradorPrice = "599.00";
    String goldenPrice = "649.00";
    String totalPrice = "1248.00";

    @Before public void
    startApplication() throws Exception {
        application.start();
        retrieversAreForSale();
    }

    @After public void
    stopApplication() throws Exception {
        application.stop();
    }

    @Test public void
    purchasesSeveralItemsUsingACreditCard() {
        application.buy("Labrador Retriever", "11111111");
        application.buy("Golden Retriever", "22222222");
        application.checkout();
        application.showsTotalToPay(totalPrice);

        application.pay("John", "Doe", "jdoe@gmail.com", "Visa", "4111111111111111", "12/12");
        application.showsTotalPaid("1248.00");
        application.showsLineItem("11111111", "Male Adult", "599.00");
        application.showsLineItem("22222222", "Female Adult", "649.00");
        application.showsBillingInformation("John", "Doe", "jdoe@gmail.com");
        application.showsCreditCardDetails("Visa", "4111111111111111", "12/12");

        application.returnShopping();
        application.showsCartIsEmpty();
    }

    private void retrieversAreForSale() throws Exception {
        application.addProduct("DOG-0001", "Labrador Retriever", "Friendly dog", "labrador.jpg");
        application.addProduct("DOG-0002", "Golden Retriever", "Joyful dog", "golden.jpg");
        application.addItem("DOG-0001", "11111111", "Male Adult", labradorPrice);
        application.addItem("DOG-0002", "22222222", "Female Adult", goldenPrice);
    }
}
