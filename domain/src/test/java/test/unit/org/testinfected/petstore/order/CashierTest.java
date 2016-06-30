package test.unit.org.testinfected.petstore.order;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.Cashier;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.testinfected.petstore.transaction.AbstractTransactor;
import org.testinfected.petstore.transaction.UnitOfWork;
import org.testinfected.petstore.validation.ConstraintViolationException;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validCreditCardDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class CashierTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    OrderNumberSequence sequence = context.mock(OrderNumberSequence.class);

    OrderBook orderBook = context.mock(OrderBook.class);
    Cart cart = new Cart();
    States transaction = context.states("transaction").startsAs("not started");

    Cashier cashier = new Cashier(sequence, orderBook, new StubTransactor(transaction));

    String BLANK = "";
    String MISSING = null;

    @Test public void
    acceptsPaymentAndRecordsOrder() throws Exception {
        cart.add(anItem().withNumber("00000100").priced("100.00").build());
        cart.add(anItem().withNumber("00000100").priced("100.00").build());
        cart.add(anItem().withNumber("00000111").describedAs("White lizard").build());

        final OrderNumber nextNumber = new OrderNumber("11223344");
        PaymentMethod paymentMethod = validCreditCardDetails().build();

        context.checking(new Expectations() {{
            allowing(sequence).nextOrderNumber(); will(returnValue(nextNumber)); when(transaction.is("started"));
            oneOf(orderBook).record(with(anOrder(
                    withNumber(nextNumber.getNumber()),
                    withSameItemCountAs(cart),
                    withSameTotalAs(cart),
                    paid()))); when(transaction.is("started"));
        }});

        assertThat("order number", cashier.placeOrder(cart, paymentMethod), equalTo(nextNumber));
        assertThat("cart not empty", cart.empty());
    }

    @Test(expected = ConstraintViolationException.class) public void
    rejectsInvalidPaymentDetails() throws Exception {
        PaymentMethod paymentMethod = aVisa().withNumber(BLANK).withExpiryDate(MISSING).billedTo(
                anAddress().withFirstName(MISSING).withLastName(MISSING))
                .build();
        cashier.placeOrder(cart, paymentMethod);
    }

    @SafeVarargs
    private final Matcher<Order> anOrder(Matcher<? super Order>... matchers) {
        return allOf(matchers);
    }

    private Matcher<? super Order> withSameItemCountAs(Cart cart) {
        return new FeatureMatcher<Order, Integer>(equalTo(cart.getItems().size()), "an order with line item count", "line item count") {
            protected Integer featureValueOf(Order actual) {
                return actual.getLineItemCount();
            }
        };
    }

    private Matcher<? super Order> withSameTotalAs(Cart cart) {
        return new FeatureMatcher<Order, BigDecimal>(equalTo(cart.getGrandTotal()), "an order with line item count", "line item count") {
            protected BigDecimal featureValueOf(Order actual) {
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

    private class StubTransactor extends AbstractTransactor {
        private final States transaction;

        public StubTransactor(States transaction) {
            this.transaction = transaction;
        }

        public void perform(UnitOfWork work) throws Exception {
            transaction.become("started");
            work.execute();
            transaction.become("committed");
        }
    }
}
