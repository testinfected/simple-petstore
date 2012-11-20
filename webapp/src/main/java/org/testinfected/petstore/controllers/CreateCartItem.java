package org.testinfected.petstore.controllers;

import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.Controller;

public class CreateCartItem implements Controller {

    private final SalesAssistant salesAssistant;

    public CreateCartItem(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void process(Request request, Response response) throws Exception {
        String number = request.getParameter("item-number");
        salesAssistant.addToCart(new ItemNumber(number));
        response.redirectTo("/cart");
    }
}
