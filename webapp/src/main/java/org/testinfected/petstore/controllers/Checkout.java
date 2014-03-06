package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.util.SessionScope;
import org.testinfected.petstore.views.Bill;

import java.math.BigDecimal;

public class Checkout implements Application {
    private final Page checkoutPage;

    public Checkout(Page checkoutPage) {
        this.checkoutPage = checkoutPage;
    }

    public void handle(Request request, Response response) throws Exception {
        checkoutPage.render(response, new Bill().ofTotal(currentCartTotal(request)));
    }

    private BigDecimal currentCartTotal(Request request) {
        return SessionScope.cart(request).getGrandTotal();
    }
}
