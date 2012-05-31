package test.system.com.pyxis.petstore.old;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.an;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class NavigateSiteFeature extends FeatureTemplate {

    @Before public void
    inventoryIsNotEmpty() {
        Product iguana = aProduct().named("Iguana").build();
        context.given(iguana);
        Product salamander = aProduct().named("Salamander").build();
        context.given(salamander);
        context.given(an(iguana).withNumber("12345678").priced("50.00"), a(salamander));
    }

    @Test public void
    stopsBrowsingCatalog() {
        petstore.consultInventoryOf("Iguana");
        petstore.continueShopping();
        petstore.returnHome();
    }

    @Test public void
    reviewsCartContentWhileShopping() {
        petstore.consultInventoryOf("Iguana");
        petstore.buy("12345678");
        petstore.continueShopping();

        petstore.consultInventoryOf("Salamander");
        petstore.reviewCart();
    }
}