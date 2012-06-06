package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.resource.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.simpleframework.http.Status.INTERNAL_SERVER_ERROR;

public class Application implements Resource {

    private final Renderer renderer;
    private final String charset;

    public Application(Renderer renderer, String charset) {
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(Request request, Response response) {
        renderHeaders(response);
        try {
            renderBody(response);
        } catch (Exception e) {
            renderInternalError(e, response);
        } finally {
            Streams.close(response);
        }
    }

    private void renderHeaders(Response response) {
        long time = System.currentTimeMillis();
        response.set("Server", "JPetStore/0.1 (Simple 4.1.21)");
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);
    }

    private void renderBody(Response response) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset);
        Map<String, String> context = new HashMap<String, String>();
        context.put("title", "PetStore");
        renderer.render("layout/main", context, response);
    }

    private void renderInternalError(Exception error, Response response) {
        try {
            response.reset();
            response.setText(INTERNAL_SERVER_ERROR.getDescription());
            response.setCode(INTERNAL_SERVER_ERROR.getCode());
            response.set("Content-Type", "text/html; charset=" + charset);
            renderer.render("500", error, response);
        } catch (IOException ignored) {
        }
    }
}
