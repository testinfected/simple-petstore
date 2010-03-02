package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.system.com.pyxis.petstore.page.HomePage;
import test.system.com.pyxis.petstore.page.ProductsPage;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.ApplicationContext.sessionFactory;

public class SearchFeature {

    PetStoreDriver petstore = new PetStoreDriver();
    Database database = Database.connect(sessionFactory());
    HomePage home;

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
        Product aLabrador = aProduct().withName("Labrador Retriever").withPhotoUrl("/labrador.jpg").build();
        Product aChesapeake = aProduct().withName("Chesapeake").describedAs("Chesapeake bay retriever").build();
        Product aDalmatian = aProduct().withName("Dalmatian").build();
        given(aLabrador, aChesapeake, aDalmatian);
        ProductsPage resultsPage = home.searchFor("retriever");
        resultsPage.displays(aLabrador, aChesapeake);
    }

    @After
    public void tearDown() {
        petstore.close();
    }

    private <T> void given(T... entities) throws Exception {
        database.persist(entities);
    }

    private void given(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }
}
