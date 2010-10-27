package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.page.*;

import java.math.BigDecimal;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class PurchaseFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    DatabaseDriver database = new DatabaseDriver();

    BigDecimal total;

    @Before public void
    startApplication() throws Exception {
        database.start();
        setupContext();
        petstore.start();
    }

    @After public void
    stopApplication() {
        petstore.stop();
        database.stop();
    }

    private void setupContext() throws Exception {
        Product labrador = aProduct().withName("Labrador Retriever").build();
        Product golden = aProduct().withName("Golden Retriever").build();
        database.given(labrador, golden);
        database.given(
                a(labrador).withNumber("11111111").describedAs("Male Adult").priced("599.00"),
                a(golden).withNumber("22222222").describedAs("Female Adult").priced("649.00"));
        total = new BigDecimal("1248.00");
    }

    @Test public void
    purchasesSeveralItemsUsingACreditCard() throws Exception {
        havingAddedToCart("Labrador Retriever", "11111111");
        havingAddedToCart("Golden Retriever", "22222222");

        petstore.viewCart();
        petstore.checkout();
        petstore.showsTotalToPay(total);
        petstore.billTo("John", "Leclair", "jleclair@gmail.com");
        petstore.payUsingCreditCard("Visa", "9999 9999 9999 9999", "12/12");

        petstore.confirmOrder();
        petstore.showsTotalPaid(total);
        petstore.showsLineItem("11111111", "Male Adult", "599.00");
        petstore.showsLineItem("22222222", "Female Adult", "649.00");
        petstore.showsBillingInformation("John", "Leclair", "jleclair@gmail.com");
        petstore.showsCreditCardDetails("Visa", "9999 9999 9999 9999", "12/12");

        petstore.returnShopping();
        petstore.showsCartIsEmpty();
    }

    private void havingAddedToCart(String product, String itemNumber) {
        petstore.searchFor(product);
        petstore.browseItemsOf(product);
        petstore.addToCart(itemNumber);
        petstore.continueShopping();
    }
}
