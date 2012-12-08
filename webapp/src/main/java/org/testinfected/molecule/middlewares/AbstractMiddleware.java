package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Middleware;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

public abstract class AbstractMiddleware implements Middleware {

    protected Application successor = new Application() {
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
