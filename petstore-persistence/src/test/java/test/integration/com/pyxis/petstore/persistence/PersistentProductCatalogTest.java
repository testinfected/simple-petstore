package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.db.PersistenceContext;
import test.support.com.pyxis.petstore.matchers.HasFieldWithValue;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class PersistentProductCatalogTest {

    ProductCatalog productCatalog = PersistenceContext.productCatalog();
    Database database = Database.connect(PersistenceContext.sessionFactory());

    @Before
    public void cleanDatabase() {
        database.clean();
    }

    @After
    public void closeDatabase() {
        database.disconnect();
    }

    @Test
    public void wontFindAnythingIfNoProductNameMatches() throws Exception {
        havingPersisted(aProduct().withName("Dalmatian"));

        List<Product> matchingProducts = productCatalog.findProductsByKeyword("bulldog");
        assertTrue(matchingProducts.isEmpty());
    }

    @Test
    public void canFindProductsByMatchingNames() throws Exception {
        havingPersisted(
                aProduct().withName("English Bulldog"),
                and(aProduct().withName("French Bulldog")),
                and(aProduct().withName("Labrador Retriever"))
        );

        Collection<Product> matches = productCatalog.findProductsByKeyword("bull");
        assertThat(matches, hasSize(equalTo(2)));
        assertThat(matches, containsProducts(productNamed("English Bulldog"), productNamed("French Bulldog")));
    }

    @Test
    public void canFindProductsByMatchingDescription() throws Exception {
    	havingPersisted(
    			aProduct().withName("Labrador").describedAs("Friendly"),
    			and(aProduct().withName("Poodle").describedAs("Annoying"))
    	);
    	
    	List<Product> matches = productCatalog.findProductsByKeyword("friend");
    	assertThat(matches, containsProducts(productNamed("Labrador")));
    }
    
    private void havingPersisted(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private EntityBuilder<?> and(EntityBuilder<?> builder) {
        return builder;
    }

    private Matcher<Iterable<Product>> hasSize(Matcher<? super Integer> sizeMatcher) {
        return Matchers.iterableWithSize(sizeMatcher);
    }

    private Matcher<Iterable<Product>> containsProducts(Matcher<Product>... productMatchers) {
        return Matchers.containsInAnyOrder(productMatchers);
    }

    private Matcher<Product> productNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }
}
