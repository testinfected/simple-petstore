package test.unit.org.testinfected.petstore.controllers;

import org.junit.Test;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.order.Cart;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.web.MockPage;

import static org.hamcrest.Matchers.equalTo;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class ShowCartTest {

    MockPage cartPage = new MockPage();
    ShowCart showCart = new ShowCart(cartPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    rendersCartContent() throws Exception {
        final Cart cart = aCart().containing(anItem()).build();
        storeInSession(cart);

        showCart.handle(request, response);
        cartPage.assertRenderedTo(response);
        cartPage.assertRenderingContext(equalTo(cart));
    }

    private void storeInSession(Cart cart) {
        request.session().put(Cart.class, cart);
    }
}
