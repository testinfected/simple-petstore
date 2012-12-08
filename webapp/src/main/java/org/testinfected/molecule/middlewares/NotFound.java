package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.Charsets;

public class NotFound implements Application {

    public static Application notFound() {
        return new NotFound();
    }

    public void handle(Request request, Response response) throws Exception {
        response.status(HttpStatus.NOT_FOUND);
        String body = "Not found: " + request.pathInfo();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.contentType("text/plain");
        response.contentLength(bytes.length);
        response.outputStream(bytes.length).write(bytes);
    }
}
