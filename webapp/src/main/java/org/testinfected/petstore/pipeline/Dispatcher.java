package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.dispatch.Routing;
import org.testinfected.petstore.dispatch.SimpleRequest;
import org.testinfected.petstore.dispatch.SimpleResponse;
import org.testinfected.petstore.util.Charsets;

import java.nio.charset.Charset;

public class Dispatcher implements Application {

    private final Routing routing;
    private final Renderer renderer;
    private Charset charset = Charsets.ISO_8859_1;

    public Dispatcher(Routing routing, Renderer renderer) {
        this.renderer = renderer;
        this.routing = routing;
    }

    public void setEncoding(Charset charset) {
        this.charset = charset;
    }

    public void handle(Request request, Response response) throws Exception {
        routing.dispatch(new SimpleRequest(request), new SimpleResponse(response, renderer, charset));
    }
}
