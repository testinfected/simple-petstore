package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.Charsets;

public class NotFound implements Application {

    public void handle(Request request, Response response) throws Exception {
        response.setCode(Status.NOT_FOUND.getCode());
        response.setText(Status.NOT_FOUND.getDescription());
        String body = "Not found: " + request.getPath().getPath();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.set("Content-Type", "text/plain");
        response.getOutputStream(bytes.length).write(bytes);
    }
}
