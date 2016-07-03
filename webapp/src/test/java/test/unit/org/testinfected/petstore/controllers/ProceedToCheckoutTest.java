package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.session.Session;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.controllers.ProceedToCheckout;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.views.Checkout;
import test.support.org.testinfected.petstore.builders.CartBuilder;
import test.support.org.testinfected.petstore.web.MockView;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class ProceedToCheckoutTest {
    MockView<Checkout> view = new MockView<>();
    ProceedToCheckout checkout = new ProceedToCheckout(view);

    Request request = new Request();
    Response response = new Response();

    @Before
    public void
    createSession() {
        new Session().bind(request);
    }

    @Test public void
    rendersBillWithAmountOfCartGrandTotal() throws Exception {
        final BigDecimal total = new BigDecimal("324.98");
        storeInSession(aCart().containing(anItem().priced(total)));

        checkout.handle(request, response);

        view.assertRenderedTo(response);
        view.assertRenderedWith(billWithTotal(total));
    }

    private Matcher<Object> billWithTotal(BigDecimal amount) {
        return hasProperty("total", equalTo(amount));
    }

    private void storeInSession(CartBuilder cart) {
        Session.get(request).put(Cart.class, cart.build());
    }
}