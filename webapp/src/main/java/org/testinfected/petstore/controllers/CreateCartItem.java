package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.util.SessionScope;

public class CreateCartItem implements Application {

    private final ItemInventory inventory;

    public CreateCartItem(ItemInventory inventory) {
        this.inventory = inventory;
    }

    public void handle(Request request, Response response) throws Exception {
        String number = request.parameter("item-number");
        SessionScope.cartFor(request).add(inventory.find(new ItemNumber(number)));
        response.redirectTo("/cart");
    }
}
