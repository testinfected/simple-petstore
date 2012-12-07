package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.Page;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import static org.testinfected.petstore.util.Context.emptyContext;

public class StaticPage implements Controller {

    private final Page page;

    public StaticPage(Page page) {
        this.page = page;
    }

    public void handle(Request request, Response response) throws Exception {
        page.render(response, emptyContext());
    }
}
