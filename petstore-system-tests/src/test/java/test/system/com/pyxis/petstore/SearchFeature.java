package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ProductsPage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.ApplicationContext.sessionFactory;

public class SearchFeature {

    private static final List<Object> NO_RESULT = Collections.emptyList();

    private PetStoreDriver petstore = new PetStoreDriver();
    private Database database = Database.connect(sessionFactory());
    private HomePage home;

    @Before
    public void setUp() throws Exception {
        database.clean();
        home = petstore.start();
    }

    @Test
    public void displaysAnEmptyProductListWhenNoProductMatches() throws Exception {
        given(aProduct().withName("Labrador Retriever"));
        ProductsPage resultsPage = home.searchFor("Dalmatian");
        resultsPage.displays(NO_RESULT);
    }

    @Test
    public void displaysAListOfProductsWhoseNameIncludeKeyword() throws Exception {
        given(aProduct().withName("Labrador Retriever"),
              aProduct().withName("Golden Retriever"));
        ProductsPage resultsPage = home.searchFor("retriever");
        resultsPage.displays(listWithProducts("Labrador Retriever", "Golden Retriever"));
    }

    @After
    public void tearDown() {
        petstore.close();
    }

    private void given(EntityBuilder... builders) throws Exception {
        database.persist(builders);
    }

    private static List<String> listWithProducts(String... productNames) {
        return Arrays.asList(productNames);
    }

}
