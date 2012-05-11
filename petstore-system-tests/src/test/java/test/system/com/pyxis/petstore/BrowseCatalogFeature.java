package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

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
        legacyPetstore.consultInventoryOf("Iguana");
        legacyPetstore.showsNoItemAvailable();
    }

    @Test public void
    consultsAProductAvailableItems() {
        legacyContext.given(an(iguana).withNumber("12345678").describedAs("Green Adult").priced("18.50"));

        legacyPetstore.consultInventoryOf("Iguana");
        legacyPetstore.displaysItem("12345678", "Green Adult", "18.50");
        legacyPetstore.continueShopping();
    }
}
