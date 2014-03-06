package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.CoreMatchers;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.helpers.ChoiceOfCreditCards;
import org.testinfected.petstore.helpers.ListOfErrors;
import org.testinfected.petstore.helpers.Messages;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.util.BundledMessages;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.web.LegacyMockPage;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validCreditCardDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class PlaceOrderTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    LegacyMockPage checkoutPage = new LegacyMockPage();
    Messages messages = new BundledMessages(ResourceBundle.getBundle("ValidationMessages"));

    PlaceOrder placeOrder = new PlaceOrder(salesAssistant, checkoutPage, messages);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String EMPTY = "";
    String orderNumber = "12345678";

    @Test public void
    placesOrderWhenPaymentDetailsAreValidAndRedirectsToReceiptPage() throws Exception {
        final CreditCardDetails validPaymentDetails = validCreditCardDetails().build();
        captureInForm(validPaymentDetails);
        final Cart cart = aCart().containing(anItem()).build();
        storeInSession(cart);

        context.checking(new Expectations() {{
            oneOf(salesAssistant).placeOrder(with(same(cart)), with(samePaymentMethodAs(validPaymentDetails))); will(returnValue(new OrderNumber(orderNumber)));
        }});

        placeOrder.handle(request, response);
        response.assertRedirectedTo("/orders/" + orderNumber);
    }

    @SuppressWarnings("unchecked") @Test public void
    rejectsInvalidPaymentDetailsAndRendersCheckoutPageWithValidationErrors() throws Exception {
        CreditCardDetails incompletePaymentDetails = validCreditCardDetails().but().withNumber(EMPTY).build();
        captureInForm(incompletePaymentDetails);
        final BigDecimal total = new BigDecimal("324.98");
        storeInSession(aCart().containing(anItem().priced(total)).build());

        context.checking(new Expectations() {{
            never(salesAssistant).placeOrder(with(any(Cart.class)), with(any(PaymentMethod.class)));
        }});

        placeOrder.handle(request, response);

        checkoutPage.assertRenderedTo(response);
        checkoutPage.assertRenderedWith("total", total);
        checkoutPage.assertRenderedWith("cardTypes", ChoiceOfCreditCards.all().select(incompletePaymentDetails.getCardType()));
        checkoutPage.assertRenderedWith(equalTo("payment"), samePaymentMethodAs(incompletePaymentDetails));
        checkoutPage.assertRenderedWith(equalTo("errors"), errors(
                withMessage("paymentDetails", "Please correct the following errors:"),
                withMessage("paymentDetails.cardNumber", "may not be empty", "not a valid number")));
    }

    private Matcher<ListOfErrors> errors(Matcher<? super ListOfErrors>... errorMatchers) {
        return CoreMatchers.allOf(errorMatchers);
    }

    private Matcher<? super ListOfErrors> withMessage(final String path, String... errors) {
        return new FeatureMatcher<ListOfErrors, Iterable<String>>(hasItems(errors), "form with errors['" + path + "']", "errors['" + path + "']") {
            protected Iterable<String> featureValueOf(ListOfErrors actual) {
                return actual.errorMessages(path);
            }
        };
    }

    private void storeInSession(Cart cart) {
        request.session().put(Cart.class, cart);
    }

    private void captureInForm(final CreditCardDetails paymentDetails) {
        request.addParameter("first-name", paymentDetails.getFirstName());
        request.addParameter("last-name", paymentDetails.getLastName());
        request.addParameter("email", paymentDetails.getEmail());
        request.addParameter("card-number", paymentDetails.getCardNumber());
        request.addParameter("card-type", paymentDetails.getCardType().toString());
        request.addParameter("expiry-date", paymentDetails.getCardExpiryDate());
    }

    private Matcher<CreditCardDetails> samePaymentMethodAs(CreditCardDetails paymentMethod) {
        return samePropertyValuesAs(paymentMethod);
    }
}
