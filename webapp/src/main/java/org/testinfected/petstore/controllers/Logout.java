package org.testinfected.petstore.controllers;

import org.testinfected.support.Application;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

public class Logout implements Application {

    public void handle(Request request, Response response) {
        response.redirectTo("/");
    }
}
