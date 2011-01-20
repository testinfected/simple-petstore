package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SearchFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    DatabaseDriver database = new DatabaseDriver();

    @Before public void
    startApplication() throws Exception {
        database.start();
        petstore.start();
    }

    @After public void
    stopApplication() throws Exception {
        petstore.stop();
        database.stop();
    }

    private void given(Builder<?>... builders) throws Exception {
        database.contain(builders);
    }

    @Test public void
    searchesForAProductNotAvailableInStore() throws Exception {
        given(aProduct().withName("Labrador Retriever"));

        petstore.searchFor("Dalmatian");
        petstore.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() throws Exception {
        given(aProduct().withNumber("LAB-1234").withName("Labrador Retriever"),
              aProduct().withNumber("CHE-5678").withName("Chesapeake").describedAs("Chesapeake bay retriever"),
              aProduct().withName("Dalmatian"));

        petstore.searchFor("retriever");
        petstore.displaysNumberOfResults(2);
        petstore.displaysProduct("LAB-1234", "Labrador Retriever");
        petstore.displaysProduct("CHE-5678", "Chesapeake");
    }

}
