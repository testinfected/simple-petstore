package org.testinfected.petstore.endpoints;

import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.EndPoint;

import static org.testinfected.petstore.util.Context.emptyContext;

public class Home implements EndPoint {

    public void process(Dispatch.Request request, Dispatch.Response response) throws Exception {
        response.render("home", emptyContext());
    }
}
