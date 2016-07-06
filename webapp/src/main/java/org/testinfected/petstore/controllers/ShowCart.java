package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;
import org.testinfected.petstore.lib.SessionScope;
import org.testinfected.petstore.order.Cart;

public class ShowCart implements Application {
    private final View<Cart> view;

    public ShowCart(View<Cart> view) {
        this.view = view;
    }

    public void handle(Request request, Response response) throws Exception {
        view.render(response, SessionScope.cart(request));
    }
}