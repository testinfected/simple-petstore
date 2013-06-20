package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.util.SessionScope;
import org.testinfected.petstore.views.ChoiceOfCreditCards;

import static org.testinfected.petstore.util.Context.context;

public class Checkout implements Application {
    private final Page checkoutPage;

    public Checkout(Page checkoutPage) {
        this.checkoutPage = checkoutPage;
    }

    public void handle(Request request, Response response) throws Exception {
        checkoutPage.render(response, context().
                with("total", SessionScope.cartFor(request).getGrandTotal()).
                and("cardTypes", ChoiceOfCreditCards.from(CreditCardType.values())).
                asMap());
    }
}
