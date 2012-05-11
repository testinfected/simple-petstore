package test.system.com.pyxis.petstore;

import org.junit.Test;
import test.support.com.pyxis.petstore.web.SystemTestContext;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SearchFeature extends FeatureTemplate {

    SystemTestContext simple = SystemTestContext.systemTesting();

    @Test public void
    searchesForAProductNotAvailableInStore() {
        legacyContext.given(aProduct().withName("Labrador Retriever"));

        legacyPetstore.searchFor("Dalmatian");
        legacyPetstore.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() {
        legacyContext.given(aProduct().withNumber("LAB-1234").withName("Labrador Retriever"),
                aProduct().withNumber("CHE-5678").withName("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().withName("Dalmatian"));

        legacyPetstore.searchFor("retriever");
        legacyPetstore.displaysNumberOfResults(2);
        legacyPetstore.displaysProduct("LAB-1234", "Labrador Retriever");
        legacyPetstore.displaysProduct("CHE-5678", "Chesapeake");
    }

}
