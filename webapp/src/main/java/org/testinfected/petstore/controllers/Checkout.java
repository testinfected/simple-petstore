package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import static org.testinfected.petstore.util.Context.context;

public class Checkout implements Controller {
    private final SalesAssistant salesAssistant;
    private final Page checkoutPage;

    public Checkout(SalesAssistant salesAssistant, Page checkoutPage) {
        this.salesAssistant = salesAssistant;
        this.checkoutPage = checkoutPage;
    }

    public void handle(Request request, Response response) throws Exception {
        checkoutPage.render(response, context().
                with("total", salesAssistant.orderTotal()).
                and("cardTypes", CreditCardType.options().entrySet()).
                asMap());
    }
}
