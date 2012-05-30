package test.system.com.pyxis.petstore.old;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.an;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ShopFeature extends FeatureTemplate {

    @Before public void
    iguanaAreForSale() {
        Product iguana = aProduct().named("Iguana").build();
        context.given(iguana);
        context.given(
                an(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"),
                an(iguana).withNumber("87654321").describedAs("Blue Female").priced("58.97"));
    }

    @Test public void
    shopsForItemsAndAddsThemToCart() {
        petstore.showsCartIsEmpty();

        petstore.buy("Iguana", "12345678");
        petstore.showsItemInCart("12345678", "Green Adult", "18.50");
        petstore.showsGrandTotal("18.50");
        petstore.showsCartTotalQuantity(1);
        petstore.continueShopping();

        petstore.buy("Iguana", "87654321");
        petstore.showsItemInCart("87654321", "Blue Female", "58.97");
        petstore.showsGrandTotal("77.47");
        petstore.showsCartTotalQuantity(2);
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() {
        petstore.buy("Iguana", "12345678");
        petstore.showsItemQuantity("12345678", 1);
        petstore.continueShopping();

        petstore.buy("Iguana", "12345678");
        petstore.showsItemQuantity("12345678", 2);
        petstore.showsCartTotalQuantity(2);
    }
}