package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.order.OrderNumber;
import com.pyxis.petstore.domain.order.SalesAssistant;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.PlaceOrder;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.validVisaDetails;

@RunWith(JMock.class)
public class PlaceOrderTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    PlaceOrder placeOrder = new PlaceOrder(salesAssistant);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

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
