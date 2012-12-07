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
import test.support.org.testinfected.support.web.MockRequest;
import test.support.org.testinfected.support.web.MockResponse;

import static test.support.org.testinfected.support.web.MockRequest.aRequest;
import static test.support.org.testinfected.support.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class CreateCartItemTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    CreateCartItem createCartItem = new CreateCartItem(salesAssistant);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    String itemNumber = "12345678";

    @Test public void
    addsItemToCartAndRedirectsToCartPage() throws Exception {
        request.addParameter("item-number", itemNumber);

        context.checking(new Expectations() {{
            oneOf(salesAssistant).addToCart(with(equal(new ItemNumber(itemNumber))));
        }});

        createCartItem.handle(request, response);

        response.assertRedirectedTo("/cart");
    }
}
