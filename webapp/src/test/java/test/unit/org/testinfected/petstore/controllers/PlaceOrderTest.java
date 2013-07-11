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
import test.support.org.testinfected.petstore.web.MockPage;

import java.math.BigDecimal;
import java.util.ListResourceBundle;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class PlaceOrderTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    MockPage checkoutPage = new MockPage();
    Messages messages = new BundledMessages(new ListResourceBundle() {
        public Locale getLocale() { return Locale.US; }

        protected Object[][] getContents() {
            return new Object[][] {
                    { "invalid.paymentDetails", "payment details are invalid" },
                    { "blank.paymentDetails.cardNumber", "card number may not be blank"}
            };
        }
    });

    PlaceOrder placeOrder = new PlaceOrder(salesAssistant, checkoutPage, messages);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String BLANK = "    ";
    String orderNumber = "12345678";

    @Test public void
    placesOrderWhenPaymentDetailsAreValidAndRedirectsToReceiptPage() throws Exception {
        final CreditCardDetails validPaymentDetails = validVisaDetails().build();
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
        CreditCardDetails incompletePaymentDetails = validVisaDetails().but().withNumber(BLANK).build();
        captureInForm(incompletePaymentDetails);
        final BigDecimal total = new BigDecimal("324.98");
        storeInSession(aCart().containing(anItem().priced(total)).build());

        context.checking(new Expectations() {{
            never(salesAssistant).placeOrder(with(any(Cart.class)), with(any(PaymentMethod.class)));
        }});

        placeOrder.handle(request, response);

        checkoutPage.assertRenderedTo(response);
        checkoutPage.assertRenderedWith("total", total);
        checkoutPage.assertRenderedWith("cardTypes", ChoiceOfCreditCards.all().select(incompletePaymentDetails.cardType()));
        checkoutPage.assertRenderedWith(equalTo("payment"), samePaymentMethodAs(incompletePaymentDetails));
        checkoutPage.assertRenderedWith(equalTo("errors"), errors(
                withMessage("paymentDetails", "payment details are invalid"),
                withMessage("paymentDetails.cardNumber", "card number may not be blank")));
    }

    private Matcher<ListOfErrors> errors(Matcher<? super ListOfErrors>... errorMatchers) {
        return CoreMatchers.allOf(errorMatchers);
    }

    private Matcher<? super ListOfErrors> withMessage(final String path, String error) {
        return new FeatureMatcher<ListOfErrors, Iterable<String>>(hasItem(error), "form with errors['" + path + "']", "errors['" + path + "']") {
            protected Iterable<String> featureValueOf(ListOfErrors actual) {
                return actual.errorMessages(path);
            }
        };
    }

    private void storeInSession(Cart cart) {
        request.session().put(Cart.class, cart);
    }

    private void captureInForm(final CreditCardDetails paymentDetails) {
        request.addParameter("first-name", paymentDetails.firstName());
        request.addParameter("last-name", paymentDetails.lastName());
        request.addParameter("email", paymentDetails.email());
        request.addParameter("card-number", paymentDetails.cardNumber());
        request.addParameter("card-type", paymentDetails.cardType().toString());
        request.addParameter("expiry-date", paymentDetails.cardExpiryDate());
    }

    private Matcher<CreditCardDetails> samePaymentMethodAs(CreditCardDetails paymentMethod) {
        return samePropertyValuesAs(paymentMethod);
    }
}
