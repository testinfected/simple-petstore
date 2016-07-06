package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;
import org.testinfected.petstore.lib.SessionScope;
import org.testinfected.petstore.views.Checkout;

import java.math.BigDecimal;

public class ProceedToCheckout implements Application {
    private final View<Checkout> view;

    public ProceedToCheckout(View<Checkout> view) {
        this.view = view;
    }

    public void handle(Request request, Response response) throws Exception {
        view.render(response, new Checkout().forTotalOf(currentCartTotal(request)));
    }

    private BigDecimal currentCartTotal(Request request) {
        return SessionScope.cart(request).getGrandTotal();
    }
}
