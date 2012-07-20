package org.testinfected.petstore.destinations;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.dispatch.Destination;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Home implements Destination {

    public void handle(Request request, Response response, Dispatcher dispatcher) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        dispatcher.renderTemplate("pages/home", context, response);
    }
}
