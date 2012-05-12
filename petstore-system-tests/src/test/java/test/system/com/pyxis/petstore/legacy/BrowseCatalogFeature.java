package test.system.com.pyxis.petstore.legacy;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.an;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class BrowseCatalogFeature extends FeatureTemplate {

    Product iguana;

    @Before public void
    iguanaAreForSale() {
        iguana = aProduct().withName("Iguana").build();
        context.given(iguana);
    }

    @Test public void
    consultsAProductCurrentlyOutOfStock() {
        petstore.consultInventoryOf("Iguana");
        petstore.showsNoItemAvailable();
    }

    @Test public void
    consultsAProductAvailableItems() {
        context.given(an(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"));

        petstore.consultInventoryOf("Iguana");
        petstore.displaysItem("12345678", "Green Adult", "18.50");
        petstore.continueShopping();
    }
}
