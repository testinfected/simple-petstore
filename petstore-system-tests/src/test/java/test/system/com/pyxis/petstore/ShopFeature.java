package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ShopFeature {

	PetStoreDriver petstore = new PetStoreDriver();
	DatabaseDriver database = new DatabaseDriver();

    @Before public void
    startApplication() throws Exception {
        petstore.start();
	}

    @After public void
    stopApplication() throws Exception {
        petstore.stop();
        database.stop();
    }

    @Before public void
    iguanaAreForSale() throws Exception {
        database.start();
        Product iguana = aProduct().withName("Iguana").build();
        database.contain(iguana);
        database.contain(
                anItem().of(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"),
                anItem().of(iguana).withNumber("87654321").describedAs("Blue Female").priced("58.97"));
    }

    @Test public void
    shopsForItemsAndAddsThemToCart() throws Exception {
        petstore.showsCartIsEmpty();

        petstore.buy("Iguana", "12345678");
        petstore.showsItemInCart("12345678", "Green Adult", "18.50");
        petstore.showsGrandTotal("18.50");
        petstore.showsCartTotalQuantity(1);

        petstore.buy("Iguana", "87654321");
        petstore.showsItemInCart("87654321", "Blue Female", "58.97");
        petstore.showsGrandTotal("77.47");
        petstore.showsCartTotalQuantity(2);
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() throws Exception {
        petstore.buy("Iguana", "12345678");
        petstore.showsItemQuantity("12345678", 1);

        petstore.buy("Iguana","12345678");
        petstore.showsItemQuantity("12345678", 2);
        petstore.showsCartTotalQuantity(2);
	}
}