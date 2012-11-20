package test.unit.org.testinfected.petstore.controllers;

import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.order.SalesAssistant;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.Checkout;

import java.math.BigDecimal;
import java.util.Map;

@RunWith(JMock.class)
public class CheckoutTest {
    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    Checkout checkout = new Checkout(salesAssistant);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

    @SuppressWarnings("unchecked")
    @Test public void
    makesOrderTotalAndCartTypesAvailableToView() throws Exception {
        final BigDecimal total = new BigDecimal("324.98");
        final Map<CreditCardType, String> cardTypes = CreditCardType.options();

        context.checking(new Expectations() {{
            allowing(salesAssistant).orderTotal(); will(returnValue(total));
            oneOf(response).render(with("checkout"), with(allOf(hasEntry("total", total), hasEntry("cardTypes", cardTypes.entrySet()))));
        }});

        checkout.process(request, response);
    }

    private Matcher<Map<String, Object>> allOf(final Matcher<? super Map<String, Object>>... matchers) {
        return AllOf.allOf(matchers);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String key, Object value) {
        return Matchers.hasEntry(key, value);
    }
}
