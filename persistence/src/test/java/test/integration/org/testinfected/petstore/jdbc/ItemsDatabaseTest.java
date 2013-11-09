package test.integration.org.testinfected.petstore.jdbc;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.db.ItemsDatabase;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.ProductsDatabase;
import org.testinfected.petstore.product.DuplicateItemException;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.transaction.QueryUnitOfWork;
import org.testinfected.petstore.transaction.Transactor;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.testinfected.petstore.db.Access.idOf;
import static org.testinfected.petstore.db.Access.productOf;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.a;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class ItemsDatabaseTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    ProductCatalog productCatalog = new ProductsDatabase(connection);

    ItemsDatabase itemsDatabase = new ItemsDatabase(connection);

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
    findsItemsByNumber() throws Exception {
        given(anItem().of(savedProductFrom(aProduct())).withNumber("12345678"));

        Item found = itemsDatabase.find(new ItemNumber("12345678"));
        assertThat("item", found, hasNumber("12345678"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsItemsByProductNumber() throws Exception {
        Product product = savedProductFrom(aProduct().withNumber("LAB-1234"));
        given(anItem().of(product), anItem().of(product));

        List<Item> availableItems = itemsDatabase.findByProductNumber("LAB-1234");
        assertThat("available items", availableItems, hasSize(2));
        assertThat("available items", availableItems, everyItem(hasProductNumber("LAB-1234")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsNothingIfProductHasNoAssociatedItemInInventory() throws Exception {
        given(anItem().of(savedProductFrom(aProduct().withNumber("DAL-5432"))));

        List<Item> availableItems = itemsDatabase.findByProductNumber(savedProductFrom(aProduct().withNumber("BOU-6789")).getNumber());
        assertThat("available items", availableItems, Matchers.<Item>empty());
    }

    @SuppressWarnings("unchecked")
    @Test public void
    canRoundTripItemsWithCompleteDetails() throws Exception {
        Collection<ItemBuilder> sampleItems = Arrays.asList(
                a(savedProductFrom(aProduct().named("Labrador").describedAs("A fun and friendly dog").withPhoto("labrador.jpg"))).
                        withNumber("12345678").describedAs("Chocolate male").priced("58.00"),
                a(savedProductFrom(aProduct())).withNumber("87654321"));

        for (final ItemBuilder item : sampleItems) {
            assertReloadsWithSameState(savedItemFrom(item));
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = DuplicateItemException.class) public void
    referenceNumberShouldBeUnique() throws Exception {
        ItemBuilder existingItem = a(savedProductFrom(aProduct().withNumber("LAB-1234")));
        given(existingItem);

        savedItemFrom(existingItem);
    }

    private void assertReloadsWithSameState(Item sample) {
        Item found = itemsDatabase.find(new ItemNumber(sample.getNumber()));
        assertThat("found by number", found, sameItemAs(sample));
    }

    private Matcher<Item> sameItemAs(Item original) {
        return allOf(hasField("id", equalTo(idOf(original).get())),
                samePropertyValuesAs(original),
                hasField("product", sameProductAs(productOf(original).get())));
    }

    private Matcher<Product> sameProductAs(Product original) {
        return allOf(hasField("id", equalTo(idOf(original).get())),
                samePropertyValuesAs(original));
    }

    private Matcher<Item> hasProductNumber(final String number) {
        return new FeatureMatcher<Item, String>(equalTo(number), "has product number", "product number") {
            @Override protected String featureValueOf(Item actual) {
                return actual.getProductNumber();
            }
        };
    }

    private Product savedProductFrom(final Builder<Product> builder) throws Exception {
        return transactor.performQuery(new QueryUnitOfWork<Product>() {
            public Product query() throws Exception {
                Product product = builder.build();
                productCatalog.add(product);
                return product;
            }
        });
    }

    private void given(final Builder<Item>... items) throws Exception {
        for (final Builder<Item> item : items) {
            savedItemFrom(item);
        }
    }

    private Item savedItemFrom(final Builder<Item> itemBuilder) throws Exception {
        return transactor.performQuery(new QueryUnitOfWork<Item>() {
            public Item query() throws Exception {
                Item item = itemBuilder.build();
                itemsDatabase.add(item);
                return item;
            }
        });
    }

    private Matcher<Item> hasNumber(final String number) {
        return new FeatureMatcher<Item, String>(equalTo(number), "has number", "number") {
            @Override protected String featureValueOf(Item actual) {
                return actual.getNumber();
            }
        };
    }
}
