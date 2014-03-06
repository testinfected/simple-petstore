package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.controllers.Checkout;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.views.Bill;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.CartBuilder;
import test.support.org.testinfected.petstore.web.MockPage;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class CheckoutTest {
    MockPage checkoutPage = new MockPage();
    Checkout checkout = new Checkout(checkoutPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @SuppressWarnings("unchecked") @Test public void
    rendersBillWithAmountOfCartGrandTotal() throws Exception {
        final BigDecimal total = new BigDecimal("324.98");
        storeInSession(aCart().containing(anItem().priced(total)));

        checkout.handle(request, response);

        checkoutPage.assertRenderedTo(response);
        checkoutPage.assertRenderingContext(billWithTotal(total));
    }

    private Matcher<Bill> billWithTotal(BigDecimal amount) {
        return hasProperty("total", equalTo(amount));
    }

    private void storeInSession(CartBuilder cart) {
        request.session().put(Cart.class, cart.build());
    }
}
