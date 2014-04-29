package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.util.SessionScope;

public class ShowCart implements Application {
    private final Page cartPage;

    public ShowCart(Page cartPage) {
        this.cartPage = cartPage;
    }

    public void handle(Request request, Response response) throws Exception {
        cartPage.render(response, SessionScope.cart(request));
    }
}
