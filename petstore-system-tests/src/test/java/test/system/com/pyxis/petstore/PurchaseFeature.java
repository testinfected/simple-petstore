package test.system.com.pyxis.petstore;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;

import com.pyxis.petstore.domain.product.Product;

public class PurchaseFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    DatabaseDriver database = new DatabaseDriver();

    @Before public void
    labradorsAreForSale() throws Exception {
        database.start();
        Product labrador = aProduct().withName("Labrador Retriever").build();
        Product golden = aProduct().withName("Golden Retriever").build();
        database.contain(labrador, golden);
        database.contain(
                a(labrador).withNumber("11111111").describedAs("Male Adult").priced("599.00"),
                a(golden).withNumber("22222222").describedAs("Female Adult").priced("649.00"));
    }

    @Before public void
    startApplication() throws Exception {
        petstore.start();
    }

    @After public void
    stopApplication() throws Exception {
        petstore.stop();
        database.stop();
    }

    @Test public void
    purchasesSeveralItemsUsingACreditCard() throws Exception {
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
