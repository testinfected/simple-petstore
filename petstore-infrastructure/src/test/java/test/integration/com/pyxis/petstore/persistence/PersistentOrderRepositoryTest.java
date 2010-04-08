package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.order.CartItem;
import com.pyxis.petstore.domain.order.LineItem;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderRepository;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.builders.OrderBuilder;
import test.support.com.pyxis.petstore.db.Database;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static test.support.com.pyxis.petstore.builders.AccountBuilder.anAccount;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;
import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class PersistentOrderRepositoryTest {

    Database database = Database.connect(get(SessionFactory.class));
    OrderRepository orderRepository = get(OrderRepository.class);

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

    @Test(expected = ConstraintViolationException.class) public void
    lineItemsCannotBePersistedIndependently() throws Exception {
        LineItem shouldFail = LineItem.from(new CartItem(anItem().build()));
        database.persist(shouldFail);
    }

    @Test public void
    canRoundTripOrders() throws Exception {
        OrderBuilder aPendingOrder = anOrder().from(aCart().containing(
                anItem().withNumber("00000100"),
                anItem().withNumber("00000100"),
                anItem().withNumber("00000111")));
        OrderBuilder aPaidOrder = anOrder().
                from(aCart().containing(anItem())).
                billedTo(anAccount().withFirstName("John").withLastName("Leclair").withEmail("jleclair@gmail.com")).
                paidWith(aVisa().withNumber("9999 9999 9999").withExpiryDate("12 dec 2012"));
        Collection<Order> sampleOrders = Arrays.asList(aPendingOrder.build(), aPaidOrder.build());

        for (Order order : sampleOrders) {
            database.persist(order);
            database.assertCanBeReloadedWithSameStateAs(order);
        }
    }

    private void havingPersisted(Builder<?>... builders) throws Exception {
        database.persist(builders);
    }

    private void assertViolatesUniqueness(Order order) throws Exception {
        try {
            orderRepository.store(order);
            fail("Expected constraint violation");
        } catch (ConstraintViolationException expected) {
            assertTrue(true);
        }
    }
}