package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.order.Cart;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.hamcrest.Matchers.hasEntry;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class ShowCartTest {

    Mockery context = new JUnit4Mockery();
    Page cartPage = context.mock(Page.class);
    ShowCart showCart = new ShowCart(cartPage);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Test public void
    makesCartAvailableToView() throws Exception {
        final Cart cart = aCart().containing(anItem()).build();
        request.session().put(Cart.class, cart);

        context.checking(new Expectations() {{
            oneOf(cartPage).render(with(response), with(hasEntry("cart", cart)));
        }});

        showCart.handle(request, response);
    }
}
