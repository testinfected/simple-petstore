package test.system.com.pyxis.petstore.legacy;

import org.junit.Test;
import test.support.com.pyxis.petstore.web.SystemTestContext;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SearchFeature extends FeatureTemplate {

    SystemTestContext simple = SystemTestContext.systemTesting();

    @Test public void
    searchesForAProductNotAvailableInStore() {
        context.given(aProduct().withName("Labrador Retriever"));

        petstore.searchFor("Dalmatian");
        petstore.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() {
        context.given(aProduct().withNumber("LAB-1234").withName("Labrador Retriever"),
                aProduct().withNumber("CHE-5678").withName("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().withName("Dalmatian"));

        petstore.searchFor("retriever");
        petstore.displaysNumberOfResults(2);
        petstore.displaysProduct("LAB-1234", "Labrador Retriever");
        petstore.displaysProduct("CHE-5678", "Chesapeake");
    }

}
