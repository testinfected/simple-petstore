package test.integration.com.pyxis.petstore.persistence;

import static com.pyxis.matchers.persistence.SamePersistentFieldsAs.samePersistentFieldsAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.db.Database.idOf;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.builders.ProductBuilder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.db.PersistenceContext;
import test.support.com.pyxis.petstore.db.UnitOfWork;

import com.pyxis.matchers.persistence.HasFieldWithValue;
import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;

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

    @Test public void
    wontFindAnythingIfNoProductMatches() throws Exception {
        havingPersisted(aProduct().withName("Dalmatian").describedAs("A big dog"));

        List<Product> matchingProducts = productCatalog.findProductsByKeyword("bulldog");
        assertTrue(matchingProducts.isEmpty());
    }

    @Test public void
    canFindProductsByMatchingNames() throws Exception {
        havingPersisted(
                aProduct().withName("English Bulldog"),
                and(aProduct().withName("French Bulldog")),
                and(aProduct().withName("Labrador Retriever"))
        );

        Collection<Product> matches = productCatalog.findProductsByKeyword("bull");
        assertThat(matches, hasSize(equalTo(2)));
        assertThat(matches, containsProduct(productNamed("English Bulldog"), productNamed("French Bulldog")));
    }

    @Test public void
    canFindProductsByMatchingDescription() throws Exception {
        havingPersisted(
                aProduct().withName("Labrador").describedAs("Friendly"),
                and(aProduct().withName("Poodle").describedAs("Annoying"))
        );

        List<Product> matches = productCatalog.findProductsByKeyword("friend");
        assertThat(matches, containsInAnyOrder(productNamed("Labrador")));
    }

    @Test (expected = PropertyValueException.class)
    public void cannotPersistAProductWithoutAName() throws Exception {
        productCatalog.add(aProduct().withoutAName().build());
    }

    @Test
    public void canRoundTripProducts() throws Exception {
        final Collection<Product> products = Arrays.asList(
                aProduct().withName("Labrador").describedAs("Labrador Retriever").withPhoto("labrador.png").build(),
                aProduct().withName("Dalmatian").build());

        for (Product product : products) {
            productCatalog.add(product);
            assertCanBeReloadedWithSameState(product);
        }
    }

    @Test (expected = PropertyValueException.class)
    public void cannotPersistAProductWithoutANumber() throws Exception {
        productCatalog.add(aProduct().withoutANumber().build());
    }    
    
    @Test
    public void productNumberShouldBeUnique() throws Exception {
        ProductBuilder someProduct = aProduct().withNumber("123");
        database.persist(someProduct);
        try {
			productCatalog.add(someProduct.build());
			fail("Should have thrown an exception.");
		} catch (ConstraintViolationException e) {
			assertTrue(true);
		}
    }    

    private void assertCanBeReloadedWithSameState(final Product product) throws Exception {
        database.perform(new UnitOfWork() {
            public void work(Session session) throws Exception {
                Product persisted = (Product) session.get(Product.class, idOf(product));
                assertThat(persisted, samePersistentFieldsAs(product));
            }
        });
    }

    private void havingPersisted(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private EntityBuilder<?> and(EntityBuilder<?> builder) {
        return builder;
    }

    private Matcher<Iterable<Product>> containsProduct(Matcher<Product>... productMatchers) {
        return containsInAnyOrder(productMatchers);
    }

    private Matcher<Iterable<Product>> hasSize(Matcher<? super Integer> sizeMatcher) {
        return iterableWithSize(sizeMatcher);
    }

    private Matcher<Product> productNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }
}
