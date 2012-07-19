package org.testinfected.petstore.actions;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.dispatch.Destination;
import org.testinfected.petstore.pipeline.Dispatcher;

public class Logout implements Destination {

    public void handle(Request request, Response response, Dispatcher dispatcher) {
        response.setCode(Status.SEE_OTHER.getCode());
        response.setText(Status.SEE_OTHER.getDescription());
        response.set("Location", "/");
    }
}
