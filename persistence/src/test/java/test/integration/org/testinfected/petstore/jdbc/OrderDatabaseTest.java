package test.integration.org.testinfected.petstore.jdbc;

import com.pyxis.petstore.Maybe;
import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.order.LineItem;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.OrderDatabase;
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
import static org.testinfected.petstore.jdbc.Properties.idOf;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;
import static test.support.org.testinfected.petstore.jdbc.HasFieldWithValue.hasField;

public class OrderDatabaseTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    OrderDatabase orderDatabase = new OrderDatabase(connection);

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
    canRoundTripOrdersWillCompleteDetails() throws Exception {
        final Collection<Order> sampleOrders = build(
                anOrder()
        );

        for (Order sample : sampleOrders) {
            assertCanSaveAndFindByNumberWithSameState(sample);
        }
    }

    private void assertCanSaveAndFindByNumberWithSameState(Order sample) throws Exception {
        save(sample);
        Maybe<Order> found = orderDatabase.find(new OrderNumber(sample.getNumber()));
        assertThat("found by number", found.bare(), sameOrderAs(sample));
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

    private void save(final Order order) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                orderDatabase.record(order);
            }
        });
    }
}
