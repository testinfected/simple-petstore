package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.session.Session;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.order.Cart;
import test.support.org.testinfected.petstore.web.MockView;

import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class ShowCartTest {

    MockView<Cart> view = new MockView<>();
    ShowCart showCart = new ShowCart(view);

    Request request = new Request();
    Response response = new Response();

    @Before
    public void
    createSession() {
        new Session().bind(request);
    }

    @Test public void
    rendersCartContent() throws Exception {
        final Cart cart = aCart().containing(anItem()).build();
        storeInSession(cart);

        showCart.handle(request, response);
        view.assertRenderedTo(response);
        view.assertRenderedWith(sameCartAs(cart));
    }

    private Matcher<Object> sameCartAs(Cart cart) {
        return Matchers.sameInstance(cart);
    }

    private void storeInSession(Cart cart) {
        Session.get(request).put(Cart.class, cart);
    }
}