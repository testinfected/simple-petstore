package test.integration.org.testinfected.petstore.jdbc;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.OrdersDatabase;
import org.testinfected.petstore.order.LineItem;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderNumber;
import test.support.org.testinfected.petstore.builders.OrderBuilder;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.testinfected.petstore.jdbc.Access.idOf;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class OrdersDatabaseTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    OrdersDatabase orderDatabase = new OrdersDatabase(connection);

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    findsOrdersByNumber() throws Exception {
        given(anOrder().withNumber("00000100"));
        given(anOrder().withNumber("00000101"));

        Order match = orderDatabase.find(new OrderNumber("00000100"));
        assertThat("match", match, orderWithNumber("00000100"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    canRoundTripOrdersWillCompleteDetails() throws Exception {
        final Collection<Order> sampleOrders = build(
                anOrder(),
                anOrder().from(aCart().containing(
                        anItem().withNumber("00000100").priced("100.00"),
                        anItem().withNumber("00000100").priced("100.00"),
                        anItem().withNumber("00000111").describedAs("White lizard"))),
                anOrder().paidWith(validVisaDetails())
        );

        for (Order sample : sampleOrders) {
            assertCanSaveAndFindByNumberWithSameState(sample);
        }
    }

    private Matcher<? super Order> orderWithNumber(String orderNumber) {
        return new FeatureMatcher<Order, String>(equalTo(orderNumber), "an order with number", "order number") {
            @Override protected String featureValueOf(Order order) {
                return order.getNumber();
            }
        };
    }

    private void assertCanSaveAndFindByNumberWithSameState(Order sample) throws Exception {
        save(sample);
        Order found = orderDatabase.find(new OrderNumber(sample.getNumber()));
        assertThat("found by number", found, sameOrderAs(sample));
    }

    private Matcher<Order> sameOrderAs(Order original) {
        return allOf(hasField("id", equalTo(idOf(original).get())),
                hasProperty("number", equalTo(original.getNumber())),
                hasProperty("paymentMethod", samePaymentMethod(original)),
                hasProperty("lineItems", sameLineItemsAs(original)));
    }

    private Matcher<?> sameLineItemsAs(Order original) {
        return original.getLineItems().isEmpty() ? emptyIterable() : contains(linesWithSameStateAs(original));
    }

    private Matcher<PaymentMethod> samePaymentMethod(Order original) {
        return original.isPaid() ? samePropertyValuesAs(original.getPaymentMethod()) : nullValue(PaymentMethod.class);
    }

    private List<Matcher<? super LineItem>> linesWithSameStateAs(Order original) {
        List<Matcher<? super LineItem>> all = new ArrayList<Matcher<? super LineItem>>();
        for (LineItem lineItem : original.getLineItems()) {
            all.add(sameLineItemAs(lineItem));
        }
        return all;
    }

    private Matcher<? super LineItem> sameLineItemAs(final LineItem original) {
        return allOf(hasField("id", equalTo(idOf(original).get())),
                samePropertyValuesAs(original));
    }

    private void given(OrderBuilder orderBuilder) throws Exception {
        save(orderBuilder.build());
    }

    private void save(final Order order) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                orderDatabase.record(order);
            }
        });
    }
}
