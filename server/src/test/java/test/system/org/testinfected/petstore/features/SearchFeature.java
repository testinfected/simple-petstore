package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.ApplicationDriver;
import test.support.org.testinfected.petstore.web.TestEnvironment;

import static test.system.org.testinfected.petstore.features.Product.product;

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
    searchingForAProductNotAvailableInStore() throws Exception {
        application.havingProductInCatalog("LAB-1234", "Labrador Retriever", "Friendly dog", "labrador.jpg");
        application.searchShowsNoResult("Dalmatian");
    }

    @Test public void
    searchingAndFindingProductsInCatalog() throws Exception {
        application.havingProductInCatalog("LAB-1234", "Labrador Retriever", "Friendly dog", "labrador.jpg");
        application.havingProductInCatalog("CHE-5678", "Golden", "Golden retriever", "golden.jpg");
        application.havingProductInCatalog("DAL-6666", "Dalmatian", "A very tall dog", "dalmatian.jpg");
        application.searchDisplaysResults("retriever", product("LAB-1234", "Labrador Retriever"), product("CHE-5678", "Golden"));
    }
}
