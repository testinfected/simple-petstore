package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class PlaceOrderTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    PlaceOrder placeOrder = new PlaceOrder(salesAssistant);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String orderNumber = "12345678";

    @Test public void
    placesOrderAndRedirectsToReceiptPage() throws Exception {
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
