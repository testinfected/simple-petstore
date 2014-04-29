package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

public class Logout implements Application {

    public void handle(Request request, Response response) {
        response.redirectTo("/");
    }
}
