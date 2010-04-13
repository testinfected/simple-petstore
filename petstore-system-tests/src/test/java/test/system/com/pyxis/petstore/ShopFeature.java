package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.CartPage;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ItemsPage;
import test.system.com.pyxis.petstore.page.ProductsPage;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ShopFeature {

	PetStoreDriver petstore = new PetStoreDriver();
	DatabaseDriver database = new DatabaseDriver();

    HomePage homePage;

    @Before public void
    startApplication() throws Exception {
        database.start();
        homePage = petstore.start();
        seedDatabase();
	}

    private void seedDatabase() throws Exception {
        Product iguana = aProduct().withName("Iguana").build();
        database.given(iguana);
        database.given(
            anItem().of(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"),
            anItem().of(iguana).withNumber("87654321").describedAs("Blue Female").priced("58.97"));
    }

    @Test public void
    shopsForItemsAndAddsThemToCart() throws Exception {
        homePage.showsCartIsEmpty();

        ItemsPage itemsPage = havingListedItemsOf("Iguana");

        CartPage cartPage = itemsPage.addToCart("12345678");
        cartPage.showsItemInCart("12345678", "Green Adult", "18.50");
        cartPage.showsAGrandTotalOf("18.50");
        cartPage.continueShopping();
        homePage.showsCartTotalQuantityIs(1);

        itemsPage = havingListedItemsOf("Iguana");

        cartPage = itemsPage.addToCart("87654321");
        cartPage.showsItemInCart("87654321", "Blue Female", "58.97");
        cartPage.showsAGrandTotalOf("77.47");
        cartPage.continueShopping();
        homePage.showsCartTotalQuantityIs(2);
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() throws Exception {
        ItemsPage itemsPage = havingListedItemsOf("Iguana");

        CartPage cartPage = itemsPage.addToCart("12345678");
        cartPage.showsItemQuantity("12345678", 1);
        cartPage.continueShopping();

        itemsPage = havingListedItemsOf("Iguana");
        
        cartPage = itemsPage.addToCart("12345678");
        cartPage.showsItemQuantity("12345678", 2);
        cartPage.continueShopping();
	}

    @After public void
    stopApplication() {
		petstore.stop();
		database.stop();
	}

    private ItemsPage havingListedItemsOf(final String productName) {
        ProductsPage productsPage = homePage.searchFor(productName);
        return productsPage.browseItemsOf(productName);
    }
}