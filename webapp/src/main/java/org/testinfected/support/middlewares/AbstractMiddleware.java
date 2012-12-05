package org.testinfected.support.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.Middleware;

public abstract class AbstractMiddleware implements Middleware {

    protected Application successor = new Application() {
        public void handle(Request request, Response response) throws Exception {
        }
    };

    public void connectTo(Application successor) {
        this.successor = successor;
    }

    protected void forward(org.testinfected.support.Request request, org.testinfected.support.Response response) throws Exception {
        forward(request.unwrap(org.simpleframework.http.Request.class), response.unwrap(org.simpleframework.http.Response.class));
    }

    protected void forward(Request request, Response response) throws Exception {
        successor.handle(request, response);
    }
}
