package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

public class NotFound implements Application {

    public static Application notFound() {
        return new NotFound();
    }

    public void handle(Request request, Response response) throws Exception {
        response.status(HttpStatus.NOT_FOUND);
        response.contentType("text/plain; charset=" + response.charsetName());
        response.body("Not found: " + request.pathInfo());
    }
}
