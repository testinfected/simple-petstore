package test.unit.org.testinfected.petstore.controllers;

import org.junit.Test;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.order.Cart;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.web.LegacyMockPage;

import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class ShowCartTest {

    LegacyMockPage cartPage = new LegacyMockPage();
    ShowCart showCart = new ShowCart(cartPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    makesCartAvailableToView() throws Exception {
        final Cart cart = aCart().containing(anItem()).build();
        storeInSession(cart);

        showCart.handle(request, response);
        cartPage.assertRenderedTo(response);
        cartPage.assertRenderedWith("cart", cart);
    }

    private void storeInSession(Cart cart) {
        request.session().put(Cart.class, cart);
    }
}
