package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;

public class Logout implements Controller {

    public void process(Request request, Response response) {
        response.redirectTo("/");
    }
}
