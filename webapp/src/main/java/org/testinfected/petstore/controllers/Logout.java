package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.session.Session;

public class Logout implements Application {

    public void handle(Request request, Response response) {
        Session session = Session.get(request);
        if (session != null) session.invalidate();
        response.redirectTo("/").done();
    }
}
