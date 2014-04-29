package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.util.SessionScope;
import org.testinfected.petstore.views.Checkout;

import java.math.BigDecimal;

public class ProceedToCheckout implements Application {
    private final Page checkoutPage;

    public ProceedToCheckout(Page checkoutPage) {
        this.checkoutPage = checkoutPage;
    }

    public void handle(Request request, Response response) throws Exception {
        checkoutPage.render(response, new Checkout().forTotalOf(currentCartTotal(request)));
    }

    private BigDecimal currentCartTotal(Request request) {
        return SessionScope.cart(request).getGrandTotal();
    }
}
