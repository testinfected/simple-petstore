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

        ProductsPage productsPage = homePage.searchFor("Iguana");
        ItemsPage itemsPage = productsPage.browseItemsOf("Iguana");
        CartPage cartPage = itemsPage.addToCart("12345678");
        cartPage.showsItemInCart("12345678", "Green Adult", 1, "18.50");
        homePage.showsCartTotalQuantity(1);
        cartPage.showsGrandTotal("18.50");

        productsPage = homePage.searchFor("Iguana");
        itemsPage = productsPage.browseItemsOf("Iguana");
        cartPage = itemsPage.addToCart("87654321");
        cartPage.showsItemInCart("87654321", "Blue Female", 1, "58.97");

        homePage.showsCartTotalQuantity(2);
        cartPage.showsGrandTotal("77.47");
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() throws Exception {
        homePage.showsCartIsEmpty();

        ProductsPage productsPage = homePage.searchFor("Iguana");
        ItemsPage itemsPage = productsPage.browseItemsOf("Iguana");
        CartPage cartPage = itemsPage.addToCart("12345678");
        cartPage.showsItemInCart("12345678", "Green Adult", 1, "18.50");
        homePage.showsCartTotalQuantity(1);

        productsPage = homePage.searchFor("Iguana");
        itemsPage = productsPage.browseItemsOf("Iguana");
        cartPage = itemsPage.addToCart("12345678");
        cartPage.showsItemInCart("12345678", "Green Adult", 2, "37.00");
        homePage.showsCartTotalQuantity(2);
	}

	@After public void
    stopApplication() {
		petstore.stop();
		database.stop();
	}
}