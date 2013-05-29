package test.unit.org.testinfected.petstore.order;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.QueryUnitOfWork;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.CartItem;
import org.testinfected.petstore.order.Cashier;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.testinfected.petstore.product.ItemInventory;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.iterableWithSize;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CashierTest {

    Mockery context = new JUnit4Mockery();
    OrderNumberSequence sequence = context.mock(OrderNumberSequence.class);
    OrderBook orderBook = context.mock(OrderBook.class);
    ItemInventory inventory = context.mock(ItemInventory.class);
    Cart cart = new Cart();
    Transactor transactor = context.mock(Transactor.class);

    Cashier cashier = new Cashier(sequence, orderBook, cart, transactor);

    States transaction = context.states("transaction").startsAs("not started");

    @SuppressWarnings("unchecked")
    @Test public void
    knowsCurrentOrderDetails() {
        cart.add(anItem().withNumber("12345678").priced("100.00").build());
        cart.add(anItem().withNumber("87654321").priced("150.00").build());

        assertThat("order total", cashier.orderTotal(), equalTo(new BigDecimal("250.00")));
        assertThat("order content", cashier.orderContent(), hasItemCount(2));
        assertThat("order content", cashier.orderContent(), containsItems(itemWith(number("12345678")), itemWith(number("87654321"))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    acceptsPaymentAndRecordsOrder() throws Exception {
        cart.add(anItem().withNumber("00000100").priced("100.00").build());
        cart.add(anItem().withNumber("00000100").priced("100.00").build());
        cart.add(anItem().withNumber("00000111").describedAs("White lizard").build());

        final OrderNumber nextNumber = new OrderNumber("11223344");
        PaymentMethod paymentMethod = validVisaDetails().build();

        context.checking(new Expectations() {{
            oneOf(transactor).performQuery(with(aQuery())); will(performUnitOfWork());
            allowing(sequence).nextOrderNumber(); will(returnValue(nextNumber)); when(transaction.is("started"));
            oneOf(orderBook).record(with(anOrder(
                    withNumber(nextNumber.getNumber()),
                    withSameItemCountAs(cart),
                    withSameTotalAs(cart),
                    paid()))); when(transaction.is("started"));
        }});

        assertThat("order number", cashier.placeOrder(paymentMethod), equalTo(nextNumber));
        assertThat("next order", cashier.orderContent(), isEmpty());
    }

    private Matcher<Iterable<? extends CartItem>> isEmpty() {
        return emptyIterable();
    }

    private Matcher<Order> anOrder(Matcher<? super Order>... matchers) {
        return allOf(matchers);
    }

    private Matcher<? super Order> withSameItemCountAs(Cart cart) {
        return new FeatureMatcher<Order, Integer>(equalTo(cart.getItems().size()), "an order with line item count", "line item count") {
            @Override protected Integer featureValueOf(Order actual) {
                return actual.getLineItemCount();
            }
        };
    }

    private Matcher<? super Order> withSameTotalAs(Cart cart) {
        return new FeatureMatcher<Order, BigDecimal>(equalTo(cart.getGrandTotal()), "an order with line item count", "line item count") {
            @Override protected BigDecimal featureValueOf(Order actual) {
                return actual.getTotalPrice();
            }
        };
    }

    private Matcher<? super Order> paid() {
        return new FeatureMatcher<Order, Boolean>(equalTo(true), "a paid order", "paid") {
            protected Boolean featureValueOf(Order actual) {
                return actual.isPaid();
            }
        };
    }

    private Matcher<? super Order> withNumber(String number) {
        return new FeatureMatcher<Order, String>(equalTo(number), "order with number", "number") {
            protected String featureValueOf(Order actual) {
                return actual.getNumber();
            }
        };
    }

    private Matcher<Iterable<CartItem>> hasItemCount(final int count) {
        return iterableWithSize(count);
    }

    private Matcher<Iterable<CartItem>> containsItems(Matcher<? super CartItem>... cartItemMatchers) {
        return hasItems(cartItemMatchers);
    }

    private Matcher<CartItem> itemWith(Matcher<CartItem>... itemMatchers) {
        return allOf(itemMatchers);
    }

    private Matcher<CartItem> quantity(int count) {
        return new FeatureMatcher<CartItem, Integer>(equalTo(count), "an item with quantity", "item quantity") {
            @Override protected Integer featureValueOf(CartItem actual) {
                return actual.getQuantity();
            }
        };
    }

    private Matcher<CartItem> number(String number) {
        return new FeatureMatcher<CartItem, String>(equalTo(number), "an item with number", "item number") {
            @Override protected String featureValueOf(CartItem actual) {
                return actual.getItemNumber();
            }
        };
    }

    private Matcher<QueryUnitOfWork> aQuery() {
        return any(QueryUnitOfWork.class);
    }

    private PerformQuery performUnitOfWork() {
        return new PerformQuery(transaction);
    }

    private static class PerformQuery implements Action {
        private final States transaction;

        public PerformQuery(States transaction) {
            this.transaction = transaction;
        }

        public Object invoke(Invocation invocation) throws Throwable {
            QueryUnitOfWork work = (QueryUnitOfWork) invocation.getParameter(0);
            transaction.become("started");
            work.execute();
            transaction.become("committed");
            return work.result;
        }

        public void describeTo(Description description) {
            description.appendText("performs query");
        }
    }

}
