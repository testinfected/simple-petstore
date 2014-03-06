package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;

public class StaticPage implements Application {
    private final Page page;

    public StaticPage(Page page) {
        this.page = page;
    }

    public void handle(Request request, Response response) throws Exception {
        page.render(response, null);
    }
}
