package test.integration.org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.nullValue;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ProductsDatabaseTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    ProductCatalog productCatalog = new ProductsDatabase(connection);

    @Before public void
    prepareDatabase() throws Exception {
        database.prepare();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsProductsByNumber() throws Exception {
        given(aProduct().withNumber("PRD-0001"));

        Product product = productCatalog.findByNumber("PRD-0001");
        assertThat("no match", product, not(nullValue()));
        assertThat("product", product, productWithNumber("PRD-0001"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsNothingIfNoProductMatchesKeyword() throws Exception {
        given(aProduct().named("Dalmatian").describedAs("A big dog"));

        Collection<Product> matchingProducts = productCatalog.findByKeyword("bulldog");
        assertThat("matching products", matchingProducts, is(empty()));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsProductsWhoseNamesMatchKeywordIgnoringCase() throws Exception {
        given(aProduct().named("English Bulldog"),
              aProduct().named("French Bulldog"),
              aProduct().named("Labrador Retriever"));

        Collection<Product> matches = productCatalog.findByKeyword("bull");
        assertThat("matching products", matches, hasSize(equalTo(2)));
        assertThat("matches", matches, containsInAnyOrder(productNamed("English Bulldog"), productNamed("French Bulldog")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsProductsWhoseDescriptionsMatchKeywordIgnoringCase() throws Exception {
        given(aProduct().named("Labrador").describedAs("Friendly"),
              aProduct().named("Golden").describedAs("Kids best friend"),
              aProduct().named("Poodle").describedAs("Annoying"));

        List<Product> matches = productCatalog.findByKeyword("friend");
        assertThat("matching products", matches, hasSize(equalTo(2)));
        assertThat("matches", matches, containsInAnyOrder(productNamed("Labrador"), productNamed("Golden")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    retrievesCompleteProductDetails() throws Exception {
        final Collection<Product> sampleProducts = build(
                aProduct().named("Labrador").describedAs("Labrador Retriever").withPhoto("labrador.png"),
                aProduct().named("Dalmatian"));

        for (Product product : sampleProducts) {
            given(product);
            assertCanBeRetrievedWithSameState(product);
        }
    }

    private void assertCanBeRetrievedWithSameState(final Product original) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                List<Product> loaded = productCatalog.findByKeyword(original.getName());
                if (loaded.isEmpty()) throw new AssertionError("No product match");
                if (loaded.size() > 1) throw new AssertionError("Several products match");
                assertThat("product", loaded.get(0), sameProductAs(original));
            }
        });
    }

    private Matcher<Product> sameProductAs(Product original) {
        return allOf(hasProperty("number", equalTo(original.getNumber())),
                     hasProperty("name", equalTo(original.getName())),
                     hasProperty("description", equalTo(original.getDescription())),
                     hasProperty("photoFileName", equalTo(original.getPhotoFileName())));
    }

    private void given(final Builder<Product>... products) throws Exception {
        given(build(products));
    }

    private void given(final Product... products) throws Exception {
        given(asList(products));
    }

    private void given(final List<Product> products) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                for (Product product : products) {
                    productCatalog.add(product);
                }
            }
        });
    }

    private Matcher<Collection<Product>> empty() {
        return Matchers.empty();
    }

    private Matcher<Iterable<Product>> hasSize(Matcher<? super Integer> sizeMatcher) {
        return iterableWithSize(sizeMatcher);
    }

    private Matcher<Product> productNamed(String name) {
        return new FeatureMatcher<Product, String>(equalTo(name), "a product named", "product name") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getName();
            }
        };
    }

    private Matcher<Product> productWithNumber(String number) {
        return new FeatureMatcher<Product, String>(equalTo(number), "a product with number", "product number") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getNumber();
            }
        };
    }
}
