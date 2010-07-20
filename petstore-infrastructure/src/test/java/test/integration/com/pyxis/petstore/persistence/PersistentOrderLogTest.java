package test.integration.com.pyxis.petstore.persistence;

import com.natpryce.maybe.Maybe;
import com.pyxis.petstore.domain.order.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.builders.OrderBuilder;
import test.support.com.pyxis.petstore.db.Database;
import test.support.com.pyxis.petstore.db.UnitOfWork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.pyxis.matchers.jpa.PersistenceMatchers.samePersistentFieldsAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static test.support.com.pyxis.petstore.builders.AddressBuilder.anAddress;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;
import static test.support.com.pyxis.petstore.db.Database.idOf;
import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class PersistentOrderLogTest {

    Database database = Database.connect(get(SessionFactory.class));
    OrderLog orderLog = get(OrderLog.class);

    @Before
    public void cleanDatabase() {
        database.clean();
    }

    @After
    public void closeDatabase() {
        database.disconnect();
    }

    @Test public void
    orderNumberShouldBeUnique() throws Exception {
        OrderBuilder order = anOrder().withNumber("00000100");
        havingPersisted(order);

        assertViolatesUniqueness(order.build());
    }

    @Test public void
    findsOrdersByNumber() throws Exception {
        havingPersisted(anOrder().withNumber("00000100"));

        Maybe<Order> entry = orderLog.find(new OrderNumber("00000100"));
        assertThat("no match", entry.isKnown());
        for (Order order : entry) {
            assertThat("match", order, orderWithNumber("00000100"));
        }
    }

    @Test(expected = ConstraintViolationException.class) public void
    lineItemsCannotBePersistedInIsolation() throws Exception {
        LineItem shouldFail = LineItem.from(new CartItem(anItem().build()));
        database.persist(shouldFail);
    }

    @Test public void
    canRoundTripOrders() throws Exception {
        OrderBuilder aPendingOrder = anOrder();
        OrderBuilder aPaidOrder = anOrder().
                paidWith(aVisa().
                        withNumber("9999 9999 9999").
                        withExpiryDate("12 dec 2012").
                        billedTo(anAddress().
                            withFirstName("John").
                            withLastName("Leclair").
                            withEmail("jleclair@gmail.com")
                        )
                );
        Collection<Order> sampleOrders = Arrays.asList(aPendingOrder.build(), aPaidOrder.build());

        for (final Order order : sampleOrders) {
            database.persist(order);
            database.assertCanBeReloadedWithSameState(order);
            if (order.isPaid()) database.assertCanBeReloadedWithSameState(order.getPaymentMethod());
        }
    }

    @Test public void
    lineItemsAreLoggedWithOrderInCorrectOrder() throws Exception {
        final Order order = anOrder().from(aCart().containing(
                anItem().withNumber("00000100").priced("100.00"),
                anItem().withNumber("00000100").priced("100.00"),
                anItem().withNumber("00000111").describedAs("White lizard"))).build();
        database.persist(order);

        database.perform(new UnitOfWork() {
            public void work(Session session) throws Exception {
                Order reloaded = (Order) session.get(Order.class, idOf(order));
                assertThat("loaded", reloaded, sameItemCountAs(order));
                assertThat("loaded line items", reloaded.getLineItems(), contains(linesWithSameStateAs(order)));
            }
        });
    }

    private Matcher<? super Order> sameItemCountAs(Order order) {
        return new FeatureMatcher<Order, Integer>(equalTo(order.getLineItemCount()), "an order with line item count", "line item count") {
            @Override protected Integer featureValueOf(Order actual) {
                return actual.getLineItemCount();
            }
        };
    }

    private List<Matcher<? super LineItem>> linesWithSameStateAs(Order order) {
        List<Matcher<? super LineItem>> all = new ArrayList<Matcher<? super LineItem>>();
        for (LineItem lineItem : order.getLineItems()) {
            all.add(samePersistentFieldsAs(lineItem));
        }
        return all;
    }

    private void havingPersisted(Builder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private Matcher<? super Order> orderWithNumber(String orderNumber) {
        return new FeatureMatcher<Order, String>(equalTo(orderNumber), "an order with number", "order number") {
            @Override protected String featureValueOf(Order order) {
                return order.getNumber();
            }
        };
    }

    private void assertViolatesUniqueness(Order order) throws Exception {
        try {
            orderLog.record(order);
            fail("No constraint violation");
        } catch (ConstraintViolationException expected) {
        }
    }
}