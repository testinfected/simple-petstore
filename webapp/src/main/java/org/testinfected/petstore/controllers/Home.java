package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;

import static org.testinfected.petstore.util.Context.emptyContext;

public class Home implements Controller {

    public void process(Request request, Response response) throws Exception {
        response.render("home", emptyContext());
    }
}
