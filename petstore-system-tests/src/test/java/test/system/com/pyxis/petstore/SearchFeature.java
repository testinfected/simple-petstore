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
        Product aLabrador = aProduct().withName("Labrador Retriever").build();
        Product aChesapeake = aProduct().withName("Chesapeake").describedAs("Chesapeake bay retriever").build();
        Product aDalmatian = aProduct().withName("Dalmatian").build();
        database.given(aLabrador, aChesapeake, aDalmatian);
        ProductsPage resultsPage = home.searchFor("retriever");
        resultsPage.displays(aLabrador, aChesapeake);
    }

    @After
    public void tearDown() {
    	database.stop();
        petstore.close();
    }

}
