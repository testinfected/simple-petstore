package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.Product;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.*;

import java.math.BigDecimal;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class PurchaseFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    DatabaseDriver database = new DatabaseDriver();

    HomePage homePage;
    BigDecimal total;

    @Before public void
    startApplication() throws Exception {
        database.start();
        homePage = petstore.start();
        seedDatabase();
    }

    private void seedDatabase() throws Exception {
        Product labrador = aProduct().withName("Labrador Retriever").build();
        database.given(labrador);
        database.given(
            anItem().of(labrador).withNumber("11111111").priced("599.00"),
            anItem().of(labrador).withNumber("22222222").priced("649.00"));
        total = new BigDecimal("1248.00");
    }

//    @Ignore("in development")
    @Test public void
    purchasesItemsUsingCreditCard() throws Exception {
        ProductsPage productsPage = homePage.searchFor("Labrador");
        ItemsPage itemsPage = productsPage.browseItemsOf("Labrador Retriever");
        CartPage cartPage = itemsPage.addToCart("11111111");

        cartPage.continueShopping();

        productsPage = homePage.searchFor("Labrador");
        itemsPage = productsPage.browseItemsOf("Labrador Retriever");
        cartPage = itemsPage.addToCart("22222222");
        
        PurchasePage purchasePage = cartPage.checkout();
        purchasePage.showsTotalToPay(total);
        purchasePage.willBillTo("John", "Leclair", "jleclair@gmail.com");
        purchasePage.willPayUsingCreditCard("Visa", "9999 9999 9999 9999", "12/12");
        ReceiptPage receiptPage = purchasePage.confirmOrder();

//        receiptPage.showsTotalPaid(total);
//
//        homePage.showsCartIsEmpty();
//
    }
}
