package test.integration.org.testinfected.petstore.jdbc;

import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.OrderNumberDatabaseSequence;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class OrderNumberDatabaseSequenceTest {

    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);

    OrderNumberSequence orderNumberSequence = new OrderNumberDatabaseSequence(connection);

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    incrementsSequenceNumber() throws Exception {
        seedSequenceNumberWith(100);

        assertThat("next order number", nextOrderNumber(), orderNumber("00000101"));
        assertThat("next order number", nextOrderNumber(), orderNumber("00000102"));
    }

    private void seedSequenceNumberWith(final long seed) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                PreparedStatement statement = connection.prepareStatement("insert into order_numbers values (?)");
                statement.setLong(1, seed);
                assertThat("update count", statement.executeUpdate(), is(1));
            }
        });
    }

    private OrderNumber nextOrderNumber() {
        return orderNumberSequence.nextOrderNumber();
    }

    private Matcher<OrderNumber> orderNumber(final String number) {
        return new FeatureMatcher<OrderNumber, String>(equalTo(number), "an order number", "order number") {
            @Override protected String featureValueOf(OrderNumber actual) {
                return actual.getNumber();
            }
        };
    }
}
