package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.ApplicationDriver;
import test.support.org.testinfected.petstore.web.TestEnvironment;

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
        application.addProduct("LAB-1234", "Labrador Retriever", "Friendly dog", "labrador.jpg");

        application.searchFor("Dalmatian");
        application.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() throws Exception {
        application.addProduct("LAB-1234", "Labrador Retriever", "Friendly dog", "labrador.jpg");
        application.addProduct("CHE-5678", "Golden", "Golden retriever", "golden.jpg");
        application.addProduct("DAL-6666", "Dalmatian", "A very tall dog", "dalmatian.jpg");

        application.searchFor("retriever");
        application.displaysNumberOfResults(2);
        application.displaysProduct("LAB-1234", "Labrador Retriever");
        application.displaysProduct("CHE-5678", "Golden");
    }
}
