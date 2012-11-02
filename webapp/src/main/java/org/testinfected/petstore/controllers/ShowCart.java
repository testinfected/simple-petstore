package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.order.SalesAssistant;
import org.testinfected.petstore.Controller;

public class ShowCart implements Controller {
    private final SalesAssistant salesAssistant;

    public ShowCart(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void process(Request request, Response response) throws Exception {
        response.render("cart", salesAssistant.cartContent());
    }
}
