package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

public class Logout implements Controller {

    public void handle(Request request, Response response) {
        response.redirectTo("/");
    }
}
