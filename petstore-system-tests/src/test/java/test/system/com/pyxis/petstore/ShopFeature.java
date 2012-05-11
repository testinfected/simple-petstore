package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ShopFeature extends FeatureTemplate {

    @Before public void
    iguanaAreForSale() {
        Product iguana = aProduct().withName("Iguana").build();
        legacyContext.given(iguana);
        legacyContext.given(
                anItem().of(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"),
                anItem().of(iguana).withNumber("87654321").describedAs("Blue Female").priced("58.97"));
    }

    @Test public void
    shopsForItemsAndAddsThemToCart() {
        legacyPetstore.showsCartIsEmpty();

        legacyPetstore.buy("Iguana", "12345678");
        legacyPetstore.showsItemInCart("12345678", "Green Adult", "18.50");
        legacyPetstore.showsGrandTotal("18.50");
        legacyPetstore.showsCartTotalQuantity(1);
        legacyPetstore.continueShopping();

        legacyPetstore.buy("Iguana", "87654321");
        legacyPetstore.showsItemInCart("87654321", "Blue Female", "58.97");
        legacyPetstore.showsGrandTotal("77.47");
        legacyPetstore.showsCartTotalQuantity(2);
    }

    @Test public void
    shopsForTheSameItemMultipleTimes() {
        legacyPetstore.buy("Iguana", "12345678");
        legacyPetstore.showsItemQuantity("12345678", 1);
        legacyPetstore.continueShopping();

        legacyPetstore.buy("Iguana", "12345678");
        legacyPetstore.showsItemQuantity("12345678", 2);
        legacyPetstore.showsCartTotalQuantity(2);
    }
}