package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.dispatch.Action;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.dispatch.Routes;
import org.testinfected.petstore.actions.Home;
import org.testinfected.petstore.util.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;

public class Dispatcher implements Application {

    private final Routes routes;
    private final Renderer renderer;
    
    private Charset charset = Charsets.ISO_8859_1;

    public Dispatcher(Routes routes, Renderer renderer) {
        this.routes = routes;
        this.renderer = renderer;
    }

    public void handle(Request request, Response response) throws Exception {
        Action action = routes.select(request);
        if (action == null) action = new Home();
        action.execute(request, response, this);
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
