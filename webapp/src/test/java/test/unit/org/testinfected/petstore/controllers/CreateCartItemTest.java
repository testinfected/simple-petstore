package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

@RunWith(JMock.class)
public class CreateCartItemTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    CreateCartItem createCartItem = new CreateCartItem(salesAssistant);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    String itemNumber = "12345678";

    @Test public void
    addsItemToCartAndRedirectsToCartPage() throws Exception {
        addItemNumberToRequestParameters(itemNumber);

        context.checking(new Expectations() {{
            oneOf(salesAssistant).addToCart(with(equal(new ItemNumber(itemNumber))));
            oneOf(response).redirectTo("/cart");
        }});

        createCartItem.process(request, response);
    }

    private void addItemNumberToRequestParameters(final String number) {
        context.checking(new Expectations() {{
            allowing(request).parameter("item-number"); will(returnValue(number));
        }});
    }
}
