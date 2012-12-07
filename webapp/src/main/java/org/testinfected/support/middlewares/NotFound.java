package org.testinfected.support.middlewares;

import org.testinfected.support.Application;
import org.testinfected.support.HttpStatus;
import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.util.Charsets;

public class NotFound implements Application {

    public static Application notFound() {
        return new NotFound();
    }

    public void handle(Request request, Response response) throws Exception {
        response.status(HttpStatus.NOT_FOUND);
        String body = "Not found: " + request.pathInfo();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.contentType("text/plain");
        response.outputStream(bytes.length).write(bytes);
    }
}
