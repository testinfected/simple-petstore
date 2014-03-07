package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.Messages;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.helpers.ErrorMessages;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.util.BundledMessages;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.web.MockPage;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validCreditCardDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class PlaceOrderTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    MockPage checkoutPage = new MockPage();
    Messages messages = new BundledMessages(ResourceBundle.getBundle("ValidationMessages"));

    PlaceOrder placeOrder = new PlaceOrder(salesAssistant, checkoutPage, messages);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String EMPTY = "";
    String orderNumber = "12345678";

    @Test public void
    placesOrderAndShowsReceiptWhenPaymentDetailsAreValid() throws Exception {
        final CreditCardDetails validPaymentDetails = validCreditCardDetails().build();
        fillOutFormWith(validPaymentDetails);
        final Cart cart = aCart().containing(anItem()).build();
        storeInSession(cart);

        context.checking(new Expectations() {{
            oneOf(salesAssistant).placeOrder(with(same(cart)), with(samePaymentAs(validPaymentDetails)));
                will(returnValue(new OrderNumber(orderNumber)));
        }});

        placeOrder.handle(request, response);
        response.assertRedirectedTo("/orders/" + orderNumber);
    }

    @SuppressWarnings("unchecked") @Test public void
    rejectsOrderAndRendersBillWhenPaymentDetailsAreInvalid() throws Exception {
        final BigDecimal total = new BigDecimal("324.98");
        storeInSession(aCart().containing(anItem().priced(total)).build());

        context.checking(new Expectations() {{
            never(salesAssistant).placeOrder(with(any(Cart.class)), with(any(PaymentMethod.class)));
        }});

        CreditCardDetails incompletePaymentDetails =
                validCreditCardDetails().but().withNumber(EMPTY).build();
        fillOutFormWith(incompletePaymentDetails);
        placeOrder.handle(request, response);

        checkoutPage.assertRenderedTo(response);
        checkoutPage.assertRenderedWith(billWithTotal(total));
        checkoutPage.assertRenderedWith(billWithPayment(incompletePaymentDetails));
        checkoutPage.assertRenderedWith(billWithErrors(
                message("paymentDetails", "Please correct the following errors:"),
                message("paymentDetails.cardNumber", "may not be empty", "not a valid number")));
    }

    private Matcher<Object> billWithTotal(BigDecimal amount) {
        return hasProperty("total", equalTo(amount));
    }

    private Matcher<Object> billWithPayment(CreditCardDetails payment) {
        return allOf(
                hasProperty("cardNumber", equalTo(payment.getCardNumber())),
                hasProperty("cardName", equalTo(payment.getCardCommonName())),
                hasProperty("cardExpiryDate", equalTo(payment.getCardExpiryDate())),
                hasProperty("firstName", equalTo(payment.getFirstName())),
                hasProperty("lastName", equalTo(payment.getLastName())),
                hasProperty("email", equalTo(payment.getEmail())));
    }

    private Matcher<Object> billWithErrors(Matcher<? super ErrorMessages>... messages) {
        return hasProperty("errorMessages", allOf(messages));
    }

    private Matcher<? super ErrorMessages> message(final String path, String... messages) {
        return new FeatureMatcher<ErrorMessages, Iterable<String>>(containsInAnyOrder(messages),
                "has messages for '" + path + "'", "'" + path + "'") {
            protected Iterable<String> featureValueOf(ErrorMessages actual) {
                return actual.at(path);
            }
        };
    }

    private void storeInSession(Cart cart) {
        request.session().put(Cart.class, cart);
    }

    private void fillOutFormWith(final CreditCardDetails paymentDetails) {
        request.addParameter("first-name", paymentDetails.getFirstName());
        request.addParameter("last-name", paymentDetails.getLastName());
        request.addParameter("email", paymentDetails.getEmail());
        request.addParameter("card-number", paymentDetails.getCardNumber());
        request.addParameter("card-type", paymentDetails.getCardType().toString());
        request.addParameter("expiry-date", paymentDetails.getCardExpiryDate());
    }

    private Matcher<CreditCardDetails> samePaymentAs(CreditCardDetails paymentMethod) {
        return samePropertyValuesAs(paymentMethod);
    }
}
