package org.testinfected.petstore.endpoints;

import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.EndPoint;

public class Logout implements EndPoint {

    public void process(Dispatch.Request request, Dispatch.Response response) {
        response.redirectTo("/");
    }
}
