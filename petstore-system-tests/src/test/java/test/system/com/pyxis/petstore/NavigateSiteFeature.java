package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class NavigateSiteFeature extends FeatureTemplate {

    @Before public void
    inventoryIsNotEmpty() {
        Product iguana = aProduct().withName("Iguana").build();
        legacyContext.given(iguana);
        Product salamander = aProduct().withName("Salamander").build();
        legacyContext.given(salamander);
        legacyContext.given(anItem().of(iguana).withNumber("12345678").priced("50.00"), anItem().of(salamander));
    }

    @Test public void
    stopsBrowsingCatalog() {
        legacyPetstore.consultInventoryOf("Iguana");
        legacyPetstore.continueShopping();
        legacyPetstore.returnHome();
    }

    @Test public void
    reviewsCartContentWhileShopping() {
        legacyPetstore.consultInventoryOf("Iguana");
        legacyPetstore.buy("12345678");
        legacyPetstore.continueShopping();

        legacyPetstore.consultInventoryOf("Salamander");
        legacyPetstore.reviewCart();
    }
}