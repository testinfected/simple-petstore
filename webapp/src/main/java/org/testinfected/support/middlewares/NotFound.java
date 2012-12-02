package org.testinfected.support.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.support.Application;
import org.testinfected.support.util.Charsets;

public class NotFound implements Application {

    public static Application notFound() {
        return new NotFound();
    }

    public void handle(Request request, Response response) throws Exception {
        response.setCode(Status.NOT_FOUND.getCode());
        response.setText(Status.NOT_FOUND.getDescription());
        String body = "Not found: " + request.getPath().getPath();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.set("Content-Type", "text/plain");
        response.getOutputStream(bytes.length).write(bytes);
    }
}
