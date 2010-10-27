package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.page.CartPage;
import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.ItemsPage;
import test.support.com.pyxis.petstore.web.page.ProductsPage;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ShopFeature {

	PetStoreDriver petstore = new PetStoreDriver();
	DatabaseDriver database = new DatabaseDriver();

    @Before public void
    startApplication() throws Exception {
        database.start();
        petstore.start();
        setupContext();
	}

    @After public void
    stopApplication() {
        petstore.stop();
        database.stop();
    }

    private void setupContext() throws Exception {
        Product iguana = aProduct().withName("Iguana").build();
        database.given(iguana);
        database.given(
            anItem().of(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"),
            anItem().of(iguana).withNumber("87654321").describedAs("Blue Female").priced("58.97"));
    }

    @Test public void
    shopsForItemsAndAddsThemToCart() throws Exception {
        petstore.showsCartIsEmpty();

        havingListedItemsOf("Iguana");
        petstore.addToCart("12345678");
        petstore.showsItemInCart("12345678", "Green Adult", "18.50");
        petstore.showsGrandTotal("18.50");
        petstore.continueShopping();
        petstore.showsCartTotalQuantity(1);

        havingListedItemsOf("Iguana");
        petstore.addToCart("87654321");
        petstore.showsItemInCart("87654321", "Blue Female", "58.97");
        petstore.showsGrandTotal("77.47");
        petstore.continueShopping();
        petstore.showsCartTotalQuantity(2);
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() throws Exception {
        havingListedItemsOf("Iguana");
        petstore.addToCart("12345678");
        petstore.showsItemQuantity("12345678", 1);
        petstore.continueShopping();

        havingListedItemsOf("Iguana");
        petstore.addToCart("12345678");
        petstore.showsItemQuantity("12345678", 2);
        petstore.continueShopping();

        petstore.showsCartTotalQuantity(2);
	}

    private void havingListedItemsOf(final String productName) {
        petstore.searchFor(productName);
        petstore.browseItemsOf(productName);
    }
}