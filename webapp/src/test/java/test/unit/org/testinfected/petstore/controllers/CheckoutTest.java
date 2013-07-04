package test.unit.org.testinfected.petstore.controllers;

import org.junit.Test;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.controllers.Checkout;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.helpers.ChoiceOfCreditCards;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.CartBuilder;
import test.support.org.testinfected.petstore.web.MockPage;

import java.math.BigDecimal;

import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class CheckoutTest {
    MockPage checkoutPage = new MockPage();
    Checkout checkout = new Checkout(checkoutPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @SuppressWarnings("unchecked") @Test public void
    makesCartAndSupportedCardTypesAvailableToView() throws Exception {
        final BigDecimal total = new BigDecimal("324.98");
        storeInSession(aCart().containing(anItem().priced(total)));
        final ChoiceOfCreditCards cardTypes = ChoiceOfCreditCards.from(CreditCardType.values());

        checkout.handle(request, response);

        checkoutPage.assertRenderedTo(response);
        checkoutPage.assertRenderedWith("total", total);
        checkoutPage.assertRenderedWith("cardTypes", cardTypes);
    }

    private void storeInSession(CartBuilder cart) {
        request.session().put(Cart.class, cart.build());
    }
}
