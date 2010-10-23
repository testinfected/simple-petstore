package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.*;

import java.math.BigDecimal;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class PurchaseFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    DatabaseDriver database = new DatabaseDriver();

    HomePage homePage;
    BigDecimal total;

    @Before public void
    startApplication() throws Exception {
        database.start();
        setupContext();
        homePage = petstore.start();
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

        CartPage cartPage = homePage.lookAtCartContent();
        PurchasePage purchasePage = cartPage.checkout();
        purchasePage.showsTotalToPay(total);
        purchasePage.willBillTo("John", "Leclair", "jleclair@gmail.com");
        purchasePage.willPayUsingCreditCard("Visa", "9999 9999 9999 9999", "12/12");

        ReceiptPage receiptPage = purchasePage.confirmOrder();
        receiptPage.showsTotalPaid(total);
        receiptPage.showsLineItem("11111111", "Male Adult", "599.00");
        receiptPage.showsLineItem("22222222", "Female Adult", "649.00");
        receiptPage.showsBillingInformation("John", "Leclair", "jleclair@gmail.com");
        receiptPage.showsCreditCardDetails("Visa", "9999 9999 9999 9999", "12/12");

        receiptPage.continueShopping();
        homePage.showsCartIsEmpty();
    }

    private void havingAddedToCart(String product, String itemNumber) {
        ProductsPage productsPage = homePage.searchFor(product);
        ItemsPage itemsPage = productsPage.browseItemsOf(product);
        CartPage cartPage = itemsPage.addToCart(itemNumber);
        cartPage.continueShopping();
    }
}
