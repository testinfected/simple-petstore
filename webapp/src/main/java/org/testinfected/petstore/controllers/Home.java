package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import static org.testinfected.petstore.util.Context.emptyContext;

public class Home implements Controller {

    public void handle(Request request, Response response) throws Exception {
        response.render("home", emptyContext());
    }
}
