package org.testinfected.petstore.endpoints;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.pipeline.Dispatcher;

public class Logout implements EndPoint {

    public void process(Request request, Response response, Dispatcher dispatcher) {
        response.setCode(Status.SEE_OTHER.getCode());
        response.setText(Status.SEE_OTHER.getDescription());
        response.set("Location", "/");
    }
}
