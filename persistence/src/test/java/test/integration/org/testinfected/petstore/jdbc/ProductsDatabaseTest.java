package test.integration.org.testinfected.petstore.jdbc;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.ProductsDatabase;
import org.testinfected.petstore.product.DuplicateProductException;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.transaction.QueryUnitOfWork;
import org.testinfected.petstore.transaction.Transactor;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.builders.ProductBuilder;
import test.support.org.testinfected.petstore.jdbc.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.testinfected.petstore.db.Access.idOf;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class ProductsDatabaseTest {

    Database database = Database.test();
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    ProductsDatabase productsDatabase = new ProductsDatabase(connection);

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    findsProductsByNumber() throws Exception {
        given(aProduct().withNumber("PRD-0001"));

        Product match = productsDatabase.findByNumber("PRD-0001");
        assertThat("matched product", match, productWithNumber("PRD-0001"));
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

    @Test public void
    findsNothingWhenNoProductInCatalogMatchesKeyword() throws Exception {
        given(aProduct().named("Dalmatian").describedAs("A big dog"));

        Collection<Product> matches = productsDatabase.findByKeyword("bulldog");
        assertThat("matching products", matches, is(empty()));
    }

    @Test public void
    roundTripsProductsWithCompleteDetails() throws Exception {
        final Collection<ProductBuilder> sampleProducts = Arrays.asList(
                aProduct().named("Labrador").describedAs("Labrador Retriever").withPhoto("labrador.png"),
                aProduct().named("Dalmatian"));

        for (final ProductBuilder product : sampleProducts) {
            assertReloadsWithWithSameState(savedProductFrom(product));
        }
    }

    @Test(expected = DuplicateProductException.class) public void
    productNumbersAreUnique() throws Exception {
        ProductBuilder anExistingProduct = aProduct().withNumber("LAB-1234");
        given(anExistingProduct);

        productsDatabase.add(anExistingProduct.build());
    }

    private void assertReloadsWithWithSameState(Product sample) {
        Product found = productsDatabase.findByNumber(sample.getNumber());
        assertThat("found by number", found, sameProductAs(sample));
    }

    private Matcher<Product> sameProductAs(Product original) {
        return allOf(hasField("id", equalTo(idOf(original).get())),
                     samePropertyValuesAs(original));
    }

    @SafeVarargs
    private final void given(final Builder<Product>... products) throws Exception {
        for (final Builder<Product> product : products) {
            savedProductFrom(product);
        }
    }

    private Product savedProductFrom(final Builder<Product> productBuilder) throws Exception {
        return transactor.performQuery(new QueryUnitOfWork<Product>() {
            public Product query() throws Exception {
                Product product = productBuilder.build();
                productsDatabase.add(product);
                return product;
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
