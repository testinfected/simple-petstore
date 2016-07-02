package test.system.org.testinfected.petstore.features;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.org.testinfected.petstore.web.Actors;
import test.support.org.testinfected.petstore.web.actors.Administrator;
import test.support.org.testinfected.petstore.web.actors.Customer;
import test.support.org.testinfected.petstore.web.drivers.ServerDriver;

public class SearchFeature {

    ServerDriver server = new ServerDriver();
    Actors actors = new Actors();
    Administrator administrator = actors.administrator();
    Customer customer = actors.customer();

    @Before public void
    startServer() throws Exception {
        server.start();
    }

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @After public void
    stopUsingApplication() {
        customer.done();
    }

    @Test public void
    searchingForAProductNotAvailableInStore() throws Exception {
        administrator.addProductToCatalog("LAB-1234", "Labrador Retriever", "Friendly dog", "labrador.jpg");

        customer.browseCatalog()
                .searchFor("Dalmatian")
                .obtainsNoResult();
    }
}