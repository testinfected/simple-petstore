package test.integration.org.testinfected.petstore.jdbc;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.db.OrderNumberDatabaseSequence;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.OrderNumberSequence;
import test.support.org.testinfected.petstore.jdbc.Database;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrderNumberDatabaseSequenceTest {

    Database database = Database.test();
    Connection connection = database.connect();

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
        OrderNumber currentNumber = nextOrderNumber();

        assertThat("next in sequence after " + currentNumber, nextOrderNumber(), orderNumber(parse(currentNumber) + 1));
        assertThat("next in sequence after " + currentNumber, nextOrderNumber(), orderNumber(parse(currentNumber) + 2));
    }

    private int parse(OrderNumber number) {
        return Integer.parseInt(number.getNumber());
    }

    private OrderNumber nextOrderNumber() {
        return orderNumberSequence.nextOrderNumber();
    }

    private Matcher<OrderNumber> orderNumber(final int number) {
        return new FeatureMatcher<OrderNumber, Integer>(equalTo(number), "an order number", "order number") {
            protected Integer featureValueOf(OrderNumber actual) {
                return parse(actual);
            }
        };
    }
}
