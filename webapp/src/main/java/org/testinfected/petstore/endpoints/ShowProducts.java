package org.testinfected.petstore.endpoints;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowProducts implements EndPoint {

    public void process(Request request, Response response, Dispatcher dispatcher) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        dispatcher.renderTemplate("pages/products", context, response);
    }
}
