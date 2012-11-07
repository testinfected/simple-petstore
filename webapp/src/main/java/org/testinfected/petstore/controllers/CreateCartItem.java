package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.order.SalesAssistant;
import com.pyxis.petstore.domain.product.ItemNumber;
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
