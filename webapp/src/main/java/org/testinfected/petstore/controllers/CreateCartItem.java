package org.testinfected.petstore.controllers;

import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

public class CreateCartItem implements Application {

    private final SalesAssistant salesAssistant;

    public CreateCartItem(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void handle(Request request, Response response) throws Exception {
        String number = request.parameter("item-number");
        salesAssistant.addToCart(new ItemNumber(number));
        response.redirectTo("/cart");
    }
}
