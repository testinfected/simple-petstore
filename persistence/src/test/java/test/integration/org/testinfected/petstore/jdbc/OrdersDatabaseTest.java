package test.integration.org.testinfected.petstore.jdbc;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.OrdersDatabase;
import org.testinfected.petstore.order.LineItem;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.transaction.QueryUnitOfWork;
import org.testinfected.petstore.transaction.Transactor;
import test.support.org.testinfected.petstore.builders.OrderBuilder;
import test.support.org.testinfected.petstore.jdbc.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.testinfected.petstore.db.Access.idOf;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validCreditCardDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class OrdersDatabaseTest {

    Database database = Database.test();
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
        assertThat("matched order", match, orderWithNumber("00000100"));
    }

    @Test public void
    canRoundTripOrders() throws Exception {
        final Collection<OrderBuilder> sampleOrders = Arrays.asList(
                anOrder(),
                anOrder().from(aCart().containing(
                        anItem().withNumber("00000100").priced("100.00"),
                        anItem().withNumber("00000100").priced("100.00"),
                        anItem().withNumber("00000111").describedAs("White lizard"))),
                anOrder().paidWith(validCreditCardDetails())
        );

        for (OrderBuilder order : sampleOrders) {
            assertReloadsWithSameState(savedOrderFrom(order));
        }
    }

    private Matcher<? super Order> orderWithNumber(String orderNumber) {
        return new FeatureMatcher<Order, String>(equalTo(orderNumber), "an order with number", "order number") {
            protected String featureValueOf(Order order) {
                return order.getNumber();
            }
        };
    }

    private void assertReloadsWithSameState(Order sample) throws Exception {
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
        List<Matcher<? super LineItem>> all = new ArrayList<>();
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
        savedOrderFrom(orderBuilder);
    }

    private Order savedOrderFrom(final OrderBuilder orderBuilder) throws Exception {
        return transactor.performQuery(new QueryUnitOfWork<Order>() {
            public Order query() throws Exception {
                Order order = orderBuilder.build();
                orderDatabase.record(order);
                return order;
            }
        });
    }
}