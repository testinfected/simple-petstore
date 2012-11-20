package org.testinfected.petstore.controllers;

import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.Controller;

import static org.testinfected.petstore.util.Context.context;

public class ShowCart implements Controller {
    private final SalesAssistant salesAssistant;

    public ShowCart(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void process(Request request, Response response) throws Exception {
        response.render("cart", context().
                with("items", salesAssistant.orderContent()).
                and("total", salesAssistant.orderTotal()).
                asMap());
    }
}
