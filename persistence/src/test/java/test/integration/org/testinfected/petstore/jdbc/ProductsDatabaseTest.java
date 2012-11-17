package test.integration.org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
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
import org.testinfected.petstore.jdbc.Tables;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.testinfected.petstore.jdbc.Properties.idOf;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class ProductsDatabaseTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    ProductsDatabase productsDatabase = new ProductsDatabase(Tables.products(), connection);

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsProductsByNumber() throws Exception {
        given(aProduct().withNumber("PRD-0001"));

        Product product = productsDatabase.findByNumber("PRD-0001");
        assertThat("no match", product, not(nullValue()));
        assertThat("product", product, productWithNumber("PRD-0001"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsProductsInCatalogWhoseNamesMatchKeywordIgnoringCase() throws Exception {
        given(aProduct().named("English Bulldog"),
                aProduct().named("French Bulldog"),
                aProduct().named("Labrador Retriever"));

        Collection<Product> matches = productsDatabase.findByKeyword("bull");
        assertThat("matching products", matches, hasSize(equalTo(2)));
        assertThat("matches", matches, containsInAnyOrder(productNamed("English Bulldog"), productNamed("French Bulldog")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsProductsInCatalogWhoseDescriptionsMatchKeywordIgnoringCase() throws Exception {
        given(aProduct().named("Labrador").describedAs("Friendly"),
                aProduct().named("Golden").describedAs("Kids best friend"),
                aProduct().named("Poodle").describedAs("Annoying"));

        List<Product> matches = productsDatabase.findByKeyword("friend");
        assertThat("matching products", matches, hasSize(equalTo(2)));
        assertThat("matches", matches, containsInAnyOrder(productNamed("Labrador"), productNamed("Golden")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsNothingWhenNoProductInCatalogMatchesKeyword() throws Exception {
        given(aProduct().named("Dalmatian").describedAs("A big dog"));

        Collection<Product> matchingProducts = productsDatabase.findByKeyword("bulldog");
        assertThat("matching products", matchingProducts, is(empty()));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    canRoundTripProductsWithCompleteDetails() throws Exception {
        final Collection<Product> sampleProducts = build(
                aProduct().named("Labrador").describedAs("Labrador Retriever").withPhoto("labrador.png"),
                aProduct().named("Dalmatian"));

        for (final Product sample : sampleProducts) {
            save(sample);
            assertCanBeFoundByNumberWithSameState(sample);
            assertCanBeFoundByKeywordWithSameState(sample);
        }
    }

    private void assertCanBeFoundByNumberWithSameState(Product sample) {
        Product found = productsDatabase.findByNumber(sample.getNumber());
        assertThat("found by number", found, sameProductAs(sample));
    }

    private void assertCanBeFoundByKeywordWithSameState(Product sample) {
        List<Product> found = productsDatabase.findByKeyword(sample.getName());
        assertThat("found by keyword", uniqueElement(found), sameProductAs(sample));
    }

    private Product uniqueElement(List<Product> products) {
        if (products.isEmpty()) throw new AssertionError("No product matches");
        if (products.size() > 1) throw new AssertionError("Several products match");
        return products.get(0);
    }

    private Matcher<Product> sameProductAs(Product original) {
        return allOf(hasField("id", equalTo(idOf(original).get())),
                     samePropertyValuesAs(original));
    }

    private void given(final Builder<Product>... products) throws Exception {
        given(build(products));
    }

    private void given(final List<Product> products) throws Exception {
        for (final Product product : products) {
            given(product);
        }
    }

    private void given(final Product product) throws Exception {
        save(product);
    }

    private void save(final Product product) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                productsDatabase.add(product);
            }
        });
    }

    private Matcher<Collection<? extends Product>> empty() {
        return Matchers.empty();
    }

    private Matcher<Iterable<Product>> hasSize(Matcher<? super Integer> sizeMatcher) {
        return iterableWithSize(sizeMatcher);
    }

    private Matcher<? super Product> productNamed(String name) {
        return new FeatureMatcher<Product, String>(equalTo(name), "a product with name", "name") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getName();
            }
        };
    }

    private Matcher<? super Product> productWithNumber(String number) {
        return new FeatureMatcher<Product, String>(equalTo(number), "a product with number", "product number") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getNumber();
            }
        };
    }
}
