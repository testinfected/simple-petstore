package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;

public class StaticView implements Application {
    private final View<Void> view;

    public StaticView(View<Void> view) {
        this.view = view;
    }

    public void handle(Request request, Response response) throws Exception {
        view.render(response, null);
    }
}