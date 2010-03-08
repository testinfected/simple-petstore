package test.system.com.pyxis.petstore;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.support.com.pyxis.petstore.web.DatabaseDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ProductsPage;

import com.pyxis.petstore.domain.Product;

public class SearchFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    DatabaseDriver database = new DatabaseDriver();
    HomePage home;

    @Before
    public void setUp() throws Exception {
        database.start();
        home = petstore.start();
    }

    @Test public void
    displaysAnEmptyProductListWhenNoProductMatches() throws Exception {
        database.given(aProduct().withName("Labrador Retriever"));
        ProductsPage resultsPage = home.searchFor("Dalmatian");
        resultsPage.displaysNoResult();
    }

    @Test public void
    displaysAListOfProductsWhoseNameOrDescriptionIncludeKeyword() throws Exception {
        database.given(
                aProduct().withNumber("LAB-1234").withName("Labrador Retriever"),
                aProduct().withNumber("CHE-5678").withName("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().withName("Dalmatian"));
        ProductsPage resultsPage = home.searchFor("retriever");
        resultsPage.displaysNumberOfResults(2);
        resultsPage.displaysProduct("LAB-1234", "Labrador Retriever");
        resultsPage.displaysProduct("CHE-5678", "Chesapeake");
    }

    @After
    public void tearDown() {
    	database.stop();
        petstore.close();
    }

}
