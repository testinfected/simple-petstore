package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.billing.CreditCardType;
import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.SalesAssistant;
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

import java.util.Map;

import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CheckoutTest {
    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    Checkout checkout = new Checkout(salesAssistant);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

    @SuppressWarnings("unchecked")
    @Test public void
    makesCartContentAndCartTypesAvailableToView() throws Exception {
        final Cart cart = aCart().containing(anItem()).build();
        final Map<CreditCardType, String> cardTypes = CreditCardType.options();

        context.checking(new Expectations() {{
            allowing(salesAssistant).cartContent(); will(returnValue(cart));
            oneOf(response).render(with("checkout"), with(allOf(hasEntry("cart", cart), hasEntry("cardTypes", cardTypes.entrySet()))));
        }});

        checkout.process(request, response);
    }

    private Matcher<Map<String, Object>> allOf(final Matcher<? super Map<String, Object>>... matchers) {
        return AllOf.allOf(matchers);
    }

    private Matcher<Map<? extends String, ? extends Object>> hasEntry(String key, Object value) {
        return Matchers.hasEntry(key, value);
    }
}
