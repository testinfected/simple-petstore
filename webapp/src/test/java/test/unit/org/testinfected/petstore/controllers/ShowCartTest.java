package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.CartItem;
import org.testinfected.petstore.order.SalesAssistant;
import test.support.org.testinfected.support.web.MockRequest;
import test.support.org.testinfected.support.web.MockResponse;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.support.web.MockRequest.aRequest;
import static test.support.org.testinfected.support.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class ShowCartTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    Page cartPage = context.mock(Page.class);
    ShowCart showCart = new ShowCart(salesAssistant, cartPage);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Test public void
    makesOrderDetailsAvailableToView() throws Exception {
        Cart cart = aCart().containing(anItem()).build();
        final Iterable<CartItem> items = cart.getItems();
        final BigDecimal total = cart.getGrandTotal();

        context.checking(new Expectations() {{
            allowing(salesAssistant).orderContent(); will(returnValue(items));
            allowing(salesAssistant).orderTotal(); will(returnValue(total));

            oneOf(cartPage).render(with(response), with(allOf(hasEntry("items", items), hasEntry("total", total))));
        }});

        showCart.handle(request, response);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String key, Object value) {
        return Matchers.hasEntry(key, value);
    }
}
