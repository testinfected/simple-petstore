package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.support.util.Charsets;

import java.nio.charset.Charset;

public class NotFound implements Application {

    public static Application notFound() {
        return new NotFound();
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        response.status(HttpStatus.NOT_FOUND);
        String body = "Not found: " + request.pathInfo();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.contentType("text/plain");
        response.outputStream(bytes.length).write(bytes);
    }
}
