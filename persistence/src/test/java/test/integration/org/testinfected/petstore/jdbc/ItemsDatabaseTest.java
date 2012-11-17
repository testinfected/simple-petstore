package test.integration.org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemNumber;
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
import org.testinfected.petstore.jdbc.ItemsDatabase;
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

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.testinfected.petstore.jdbc.Properties.idOf;
import static org.testinfected.petstore.jdbc.Properties.productOf;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class ItemsDatabaseTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    ProductCatalog productCatalog = new ProductsDatabase(Tables.products(), connection);

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
        Product product = aProduct().build();
        givenInCatalog(product);
        givenInInventory(anItem().of(product).withNumber("12345678"));

        Item found = itemsDatabase.find(new ItemNumber("12345678"));
        assertThat("item", found, hasNumber("12345678"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsItemsByProductNumber() throws Exception {
        Product product = aProduct().withNumber("LAB-1234").build();
        givenInCatalog(product);
        givenInInventory(anItem().of(product), anItem().of(product));

        List<Item> availableItems = itemsDatabase.findByProductNumber("LAB-1234");
        assertThat("available items", availableItems, hasSize(2));
        assertThat("available items", availableItems, everyItem(hasProductNumber("LAB-1234")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    findsNothingIfProductHasNoAssociatedItemInInventory() throws Exception {
        Product productWithNoItem = aProduct().withNumber("DAL-5432").build();
        Product productWithItems = aProduct().withNumber("BOU-6789").build();
        givenInCatalog(productWithItems, productWithNoItem);
        givenInInventory(anItem().of(productWithItems));

        List<Item> availableItems = itemsDatabase.findByProductNumber(productWithNoItem.getNumber());
        assertThat("available items", availableItems, Matchers.<Item>empty());
    }

    @SuppressWarnings("unchecked")
    @Test public void
    canRoundTripItemsWithCompleteDetails() throws Exception {
        Product labrador = aProduct().named("Labrador").describedAs("A fun and friendly dog").withPhoto("labrador.jpg").build();
        Product dalmatian = aProduct().build();
        givenInCatalog(labrador, dalmatian);

        Collection<Item> sampleItems = build(
                a(labrador).withNumber("12345678").describedAs("Chocolate male").priced("58.00"),
                a(dalmatian).withNumber("87654321"));

        for (final Item item : sampleItems) {
            save(item);
            assertCanBeFoundByNumberWithSameState(item);
            assertCanBeFoundByProductNumberWithSameState(item);
        }
    }

    private void assertCanBeFoundByNumberWithSameState(Item sample) {
        Item found = itemsDatabase.find(new ItemNumber(sample.getNumber()));
        assertThat("found by number", found, sameItemAs(sample));
    }

    private void assertCanBeFoundByProductNumberWithSameState(Item sample) {
        List<Item> found = itemsDatabase.findByProductNumber(sample.getProductNumber());
        assertThat("found by product number", uniqueElement(found), sameItemAs(sample));
    }

    private Item uniqueElement(List<Item> items) {
        if (items.isEmpty()) throw new AssertionError("No item matches");
        if (items.size() > 1) throw new AssertionError("Several items match");
        return items.get(0);
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

    private void givenInCatalog(final Product... products) throws Exception {
        givenInCatalog(asList(products));
    }

    private void givenInCatalog(final List<Product> products) throws Exception {
        for (final Product product : products) givenInCatalog(product);
    }

    private void givenInCatalog(final Product product) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                productCatalog.add(product);
            }
        });
    }

    private void givenInInventory(final Builder<Item>... items) throws Exception {
        givenInInventory(build(items));
    }

    private void givenInInventory(final List<Item> items) throws Exception {
        for (final Item item : items) givenInInventory(item);
    }

    private void givenInInventory(final Item item) throws Exception {
        save(item);
    }

    private void save(final Item item) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                itemsDatabase.add(item);
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
