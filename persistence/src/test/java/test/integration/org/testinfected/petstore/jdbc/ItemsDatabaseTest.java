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
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class ItemsDatabaseTest {

    Database database = Database.test();
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

    @Test public void
    findsItemsByNumber() throws Exception {
        Product dog = savedProductFrom(aProduct());
        given(a(dog).withNumber("12345678"));

        Item found = itemsDatabase.find(new ItemNumber("12345678"));
        assertThat("matched item", found, hasNumber("12345678"));
    }

    @Test public void
    findsItemsByProductNumber() throws Exception {
        Product labrador = savedProductFrom(aProduct().withNumber("LAB-1234"));
        given(a(labrador), a(labrador));

        List<Item> availableItems = itemsDatabase.findByProductNumber("LAB-1234");
        assertThat("matching items", availableItems, hasSize(2));
        assertThat("available items", availableItems, everyItem(hasProductNumber("LAB-1234")));
    }

    @Test public void
    findsNothingIfProductHasNoAssociatedItemInInventory() throws Exception {
        Product dalmatian = savedProductFrom(aProduct().withNumber("DAL-5432"));
        given(a(dalmatian));

        List<Item> availableItems = itemsDatabase.findByProductNumber(savedProductFrom(aProduct().withNumber("BOU-6789")).getNumber());
        assertThat("available items", availableItems, Matchers.empty());
    }

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
            protected String featureValueOf(Item actual) {
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

    @SafeVarargs
    private final void given(final Builder<Item>... items) throws Exception {
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
            protected String featureValueOf(Item actual) {
                return actual.getNumber();
            }
        };
    }
}
