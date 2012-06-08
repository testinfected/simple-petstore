package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.util.Charsets;

import java.io.IOException;

public class Failsafe extends AbstractMiddleware {

    private final Renderer renderer;

    public Failsafe(Renderer renderer) {
        this.renderer = renderer;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            forward(request, response);
        } catch (Exception e) {
            renderError(e, response);
        }
    }

    private void renderError(Exception error, Response response) {
        try {
            response.reset();
            response.setText(Status.INTERNAL_SERVER_ERROR.getDescription());
            response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
            response.set("Content-Type", "text/html; charset=" + Charsets.ISO_8859_1.name().toLowerCase());
            renderTemplate(error, response);
        } catch (IOException ignored) {
        }
    }

    private void renderTemplate(Exception error, Response response) throws IOException {
        String body = renderer.render("500", error);
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.getOutputStream(bytes.length).write(bytes);
    }
}
