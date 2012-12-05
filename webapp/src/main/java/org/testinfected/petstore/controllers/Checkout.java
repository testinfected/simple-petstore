package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import static org.testinfected.petstore.util.Context.context;

public class Checkout implements Controller {
    private final SalesAssistant salesAssistant;

    public Checkout(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void process(Request request, Response response) throws Exception {
        response.render("checkout", context().
                with("total", salesAssistant.orderTotal()).
                and("cardTypes", CreditCardType.options().entrySet()).
                asMap());
    }
}
