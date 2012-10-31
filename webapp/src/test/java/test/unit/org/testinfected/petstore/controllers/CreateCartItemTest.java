package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.order.SalesAssistant;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.CreateCartItem;

@RunWith(JMock.class)
public class CreateCartItemTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    CreateCartItem createCartItem = new CreateCartItem(salesAssistant);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

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
            allowing(request).getParameter("item_number"); will(returnValue(number));
        }});
    }
}
