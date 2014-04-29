package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.product.Item;
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
        addToCart(request, inventory.find(new ItemNumber(number)));
        response.redirectTo("/cart");
    }

    private void addToCart(Request request, Item item) {
        SessionScope.cart(request).add(item);
    }
}
