package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.Application;
import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.matchers.HasFieldWithValue;
import test.support.com.pyxis.petstore.matchers.IsIterableWithSize;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class PersistentProductCatalogTest {

    ProductCatalog productCatalog = Application.productCatalog();
    Database database = Database.connect(Application.sessionFactory());

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

        List<Product> matchingProducts = productCatalog.findProductsByKeyword("Squirrel");
        assertTrue(matchingProducts.isEmpty());
    }

    @Test
    public void canFindProductsWithAGivenName() throws Exception {
        havingPersisted(
                aProduct().withName("Dalmatian"),
                and(aProduct().withName("Dalmatian")),
                and(aProduct().withName("Labrador"))
        );

        Collection<Product> matches = productCatalog.findProductsByKeyword("Dalmatian");
        assertThat(matches, hasSize(equalTo(2)));
        assertThat(matches, containsProducts(productNamed("Dalmatian"), productNamed("Dalmatian")));
    }

    private void havingPersisted(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private EntityBuilder<?> and(EntityBuilder<?> builder) {
        return builder;
    }

    private Matcher<Iterable<? super Product>> hasSize(final Matcher<? super Integer> sizeMatcher) {
        return IsIterableWithSize.withSize(sizeMatcher);
    }

    private Matcher<Iterable<Product>> containsProducts(final Matcher<Product>... productMatchers) {
        return Matchers.containsInAnyOrder(productMatchers);
    }

    private Matcher<Product> productNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }
}
