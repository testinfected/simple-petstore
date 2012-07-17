package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.util.RequestMatcher;

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
        if (routeToProducts(request)) {
            processProducts(request, response);
        } else if (routeToLogout(request)) {
            processLogout(request, response);
        } else {
            processDefault(request, response);
        }
    }

    private void processDefault(Request request, Response response) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        renderTemplate("pages/home", context, response);
    }

    private void processLogout(Request request, Response response) {
        response.setCode(Status.SEE_OTHER.getCode());
        response.setText(Status.SEE_OTHER.getDescription());
        response.set("Location", "/");
    }

    private void processProducts(Request request, Response response) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        renderTemplate("pages/products", context, response);
    }

    private boolean routeToLogout(Request request) {
        return request.getPath().getPath().equals("/logout") && request.getMethod().equals("DELETE");
    }

    private boolean routeToProducts(Request request) {
        return request.getPath().getPath().startsWith("/products");
    }

    private void renderTemplate(String name, Object context, Response response) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset.name().toLowerCase());
        String body = renderer.render(name, context);
        byte[] bytes = body.getBytes(charset);
        response.setContentLength(bytes.length);
        response.getOutputStream(bytes.length).write(bytes);
    }
}
