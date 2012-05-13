package test.system.com.pyxis.petstore.legacy;

import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ProductBuilder;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SearchFeature extends FeatureTemplate {

    @Test public void
    searchesForAProductNotAvailableInStore() {
        context.given(aProduct().named("Labrador Retriever"));

        petstore.searchFor("Dalmatian");
        petstore.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() {
        context.given(aProduct("LAB-1234").named("Labrador Retriever"),
                aProduct("CHE-5678").named("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().named("Dalmatian"));

        petstore.searchFor("retriever");
        petstore.displaysNumberOfResults(2);
        petstore.displaysProduct("LAB-1234", "Labrador Retriever");
        petstore.displaysProduct("CHE-5678", "Chesapeake");
    }

}
