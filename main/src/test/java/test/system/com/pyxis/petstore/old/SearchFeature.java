package test.system.com.pyxis.petstore.old;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.OldApplicationDriver;
import test.support.com.pyxis.petstore.web.OldSystemTestContext;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.OldSystemTestContext.systemTesting;

public class SearchFeature {

    OldSystemTestContext context = systemTesting();
    OldApplicationDriver petstore;

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

    @Before public void
    startApplication() {
        petstore = context.startApplication();
    }

    @After public void
    stopApplication() {
        context.stopApplication(petstore);
    }
}
