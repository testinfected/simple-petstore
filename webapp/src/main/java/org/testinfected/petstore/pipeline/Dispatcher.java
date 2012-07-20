package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.dispatch.Routing;
import org.testinfected.petstore.util.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;

public class Dispatcher implements Application {

    private final Routing routing;
    private final Renderer renderer;
    private Charset charset = Charsets.ISO_8859_1;

    public Dispatcher(Routing routing, Renderer renderer) {
        this.renderer = renderer;
        this.routing = routing;
    }

    public void handle(Request request, Response response) throws Exception {
        routing.dispatch(request, response, this);
    }

    public void renderTemplate(String name, Object context, Response response) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset.name().toLowerCase());
        String body = renderer.render(name, context);
        byte[] bytes = body.getBytes(charset);
        response.setContentLength(bytes.length);
        response.getOutputStream(bytes.length).write(bytes);
    }

    public void setEncoding(Charset charset) {
        this.charset = charset;
    }
}
