package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Renderer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher implements Application {

    private final Renderer renderer;
    private final Charset charset;

    public Dispatcher(Renderer renderer, Charset charset) {
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(Request request, Response response) throws Exception {
        Map<String, String> context = new HashMap<String, String>();
        context.put("title", "PetStore");
        renderTemplate("layout/main", context, response);
    }

    private void renderTemplate(String name, Object context, Response response) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset.name().toLowerCase());
        String body = renderer.render(name, context);
        byte[] bytes = body.getBytes(charset);
        response.setContentLength(bytes.length);
        response.getOutputStream(bytes.length).write(bytes);
    }
}
