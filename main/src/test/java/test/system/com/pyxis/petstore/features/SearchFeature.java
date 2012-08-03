package test.system.com.pyxis.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.TestEnvironment;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SearchFeature {

    ApplicationDriver application = new ApplicationDriver(TestEnvironment.load());

    @Before public void
    startApplication() throws Exception {
        application.start();
    }

    @After public void
    stopApplication() throws Exception {
        application.stop();
    }

    @Test public void
    searchesForAProductNotAvailableInStore() throws Exception {
        application.addProducts(aProduct().named("Labrador Retriever"));

        application.searchFor("Dalmatian");
        application.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() throws Exception {
        application.addProducts(aProduct("LAB-1234").named("Labrador Retriever"),
                aProduct("CHE-5678").named("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().named("Dalmatian"));

        application.searchFor("retriever");
        application.displaysNumberOfResults(2);
        application.displaysProduct("LAB-1234", "Labrador Retriever");
        application.displaysProduct("CHE-5678", "Chesapeake");
    }
}
