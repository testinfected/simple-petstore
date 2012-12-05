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
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;

@RunWith(JMock.class)
public class PlaceOrderTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    PlaceOrder placeOrder = new PlaceOrder(salesAssistant);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    String orderNumber = "12345678";

    @Test public void
    placesOrderAndRedirectsToReceiptPage() throws Exception {
        final CreditCardDetails validPaymentDetails = validVisaDetails().build();
        havingRequestParametersCapturing(validPaymentDetails);

        context.checking(new Expectations() {{
            oneOf(salesAssistant).placeOrder(with(samePaymentMethodAs(validPaymentDetails))); will(returnValue(new OrderNumber(orderNumber)));

            oneOf(response).redirectTo("/orders/" + orderNumber);
        }});

        placeOrder.process(request, response);
    }

    private void havingRequestParametersCapturing(final CreditCardDetails paymentDetails) {
        context.checking(new Expectations() {{
            allowing(request).getParameter("first-name"); will(returnValue(paymentDetails.getFirstName()));
            allowing(request).getParameter("last-name"); will(returnValue(paymentDetails.getLastName()));
            allowing(request).getParameter("email"); will(returnValue(paymentDetails.getEmail()));
            allowing(request).getParameter("card-number"); will(returnValue(paymentDetails.getCardNumber()));
            allowing(request).getParameter("card-type"); will(returnValue(paymentDetails.getCardType().toString()));
            allowing(request).getParameter("expiry-date"); will(returnValue(paymentDetails.getCardExpiryDate()));
        }});
    }

    private Matcher<? extends PaymentMethod> samePaymentMethodAs(CreditCardDetails paymentMethod) {
        return samePropertyValuesAs(paymentMethod);
    }
}
