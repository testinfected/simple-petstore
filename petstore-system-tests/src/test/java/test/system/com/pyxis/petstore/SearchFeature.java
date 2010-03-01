package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ProductsPage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.ApplicationContext.sessionFactory;

public class SearchFeature {

    private static final List<String> NO_RESULT = Collections.emptyList();

    private PetStoreDriver petstore = new PetStoreDriver();
    private Database database = Database.connect(sessionFactory());
    private HomePage home;

    @Before
    public void setUp() throws Exception {
        database.clean();
        home = petstore.start();
    }

    @Test public void
    displaysAnEmptyProductListWhenNoProductMatches() throws Exception {
        given(aProduct().withName("Labrador Retriever"));
        ProductsPage resultsPage = home.searchFor("Dalmatian");
        resultsPage.displaysNoResult();
    }

    @Test public void
    displaysAListOfProductsWhoseNameOrDescriptionIncludeKeyword() throws Exception {
        given(aProduct().withName("Labrador Retriever"),
              aProduct().withName("Chesapeake").describedAs("Chesapeake bay retriever"),
              aProduct().withName("Doberman"));
        ProductsPage resultsPage = home.searchFor("retriever");
        resultsPage.displays(listWithProductsNamed("Labrador Retriever", "Chesapeake"));
    }

    @After
    public void tearDown() {
        petstore.close();
    }

    private void given(EntityBuilder... builders) throws Exception {
        database.persist(builders);
    }

    private static List<String> listWithProductsNamed(String... productNames) {
        return Arrays.asList(productNames);
    }

}
