package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Page;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

import static org.testinfected.petstore.util.Context.context;

public class ShowCart implements Application {
    private final SalesAssistant salesAssistant;
    private final Page cartPage;

    public ShowCart(SalesAssistant salesAssistant, Page cartPage) {
        this.salesAssistant = salesAssistant;
        this.cartPage = cartPage;
    }

    public void handle(Request request, Response response) throws Exception {
        cartPage.render(response, context().
                with("items", salesAssistant.orderContent()).
                and("total", salesAssistant.orderTotal()).
                asMap());
    }
}
