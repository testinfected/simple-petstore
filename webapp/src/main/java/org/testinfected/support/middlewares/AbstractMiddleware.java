package org.testinfected.support.middlewares;

import org.testinfected.support.Application;
import org.testinfected.support.Middleware;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

public abstract class AbstractMiddleware implements Middleware {

    protected Application successor = new Application() {
        public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        }

        public void handle(Request request, Response response) throws Exception {
        }
    };

    public void connectTo(Application successor) {
        this.successor = successor;
    }

    protected void forward(Request request, Response response) throws Exception {
        successor.handle(request, response);
    }
}
