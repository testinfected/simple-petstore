package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SiteNavigationFeature extends Feature {

    @Before public void
    inventoryIsNotEmpty() {
        Product iguana = aProduct().withName("Iguana").build();
        context.given(iguana);
        Product salamander = aProduct().withName("Salamander").build();
        context.given(salamander);
        context.given(anItem().of(iguana).withNumber("12345678").priced("50.00"), anItem().of(salamander));
    }

    @Test public void
    abandonsBrowsingCatalog() {
        petstore.consultInventoryOf("Iguana");
        petstore.clickOnLogo();
        petstore.isHome();
        petstore.consultInventoryOf("Salamander");
        petstore.jumpHome();
        petstore.isHome();
    }

    @Test public void
    reviewCartContentWhileShopping() {
        petstore.buy("Iguana", "12345678");
        petstore.consultInventoryOf("Salamander");
        petstore.jumpToCart();
        petstore.showsGrandTotal("50.00");
    }

}