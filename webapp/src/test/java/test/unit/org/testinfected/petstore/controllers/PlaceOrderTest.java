package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.helpers.FormErrors;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.helpers.ErrorList;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.web.MockPage;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class PlaceOrderTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    MockPage checkoutPage = new MockPage();
    PlaceOrder placeOrder = new PlaceOrder(salesAssistant, checkoutPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String BLANK = "    ";
    String orderNumber = "12345678";

    @Test public void
    placesOrderWhenPaymentDetailsAreValidAndRedirectsToReceiptPage() throws Exception {
        final CreditCardDetails validPaymentDetails = validVisaDetails().build();
        addToRequest(validPaymentDetails);
        final Cart cart = aCart().containing(anItem()).build();
        storeInSession(cart);

        context.checking(new Expectations() {{
            oneOf(salesAssistant).placeOrder(with(same(cart)), with(samePaymentMethodAs(validPaymentDetails))); will(returnValue(new OrderNumber(orderNumber)));
        }});

        placeOrder.handle(request, response);
        response.assertRedirectedTo("/orders/" + orderNumber);
    }

    @Test public void
    rejectsInvalidPaymentDetailsAndRendersCheckoutPageWithValidationErrors() throws Exception {
        addToRequest(validVisaDetails().build());
        request.addParameter("card-number", BLANK);

        context.checking(new Expectations() {{
            never(salesAssistant).placeOrder(with(any(Cart.class)), with(any(PaymentMethod.class)));
        }});

        placeOrder.handle(request, response);

        checkoutPage.assertRenderedTo(response);
        checkoutPage.assertRenderedWith("errors", new ErrorList(formErrorsForBlankCardNumber()));
    }

    private FormErrors formErrorsForBlankCardNumber() {
        FormErrors errors = new FormErrors("payment");
        errors.reject("invalid");
        errors.rejectValue("cardNumber", "blank");
        return errors;
    }

    private void storeInSession(Cart cart) {
        request.session().put(Cart.class, cart);
    }

    private void addToRequest(final CreditCardDetails paymentDetails) {
        request.addParameter("first-name", paymentDetails.getFirstName());
        request.addParameter("last-name", paymentDetails.getLastName());
        request.addParameter("email", paymentDetails.getEmail());
        request.addParameter("card-number", paymentDetails.getCardNumber());
        request.addParameter("card-type", paymentDetails.getCardType().toString());
        request.addParameter("expiry-date", paymentDetails.getCardExpiryDate());
    }

    private Matcher<? extends PaymentMethod> samePaymentMethodAs(CreditCardDetails paymentMethod) {
        return samePropertyValuesAs(paymentMethod);
    }
}
