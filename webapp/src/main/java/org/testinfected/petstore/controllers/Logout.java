package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

public class Logout implements Application {

    public void handle(Request request, Response response) {
        response.redirectTo("/");
    }
}
