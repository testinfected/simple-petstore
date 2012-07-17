package org.testinfected.petstore.actions;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.dispatch.Action;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowProducts implements Action {

    public void execute(Request request, Response response, Dispatcher dispatcher) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        dispatcher.renderTemplate("pages/products", context, response);
    }
}
