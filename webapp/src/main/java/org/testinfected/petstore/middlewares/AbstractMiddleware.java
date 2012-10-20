package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;

public abstract class AbstractMiddleware implements Middleware {

    private Application app = new Application() {
        public void handle(Request request, Response response) throws Exception {
        }
    };

    public void chain(Application app) {
        this.app = app;
    }

    protected void forward(Request request, Response response) throws Exception {
        app.handle(request, response);
    }
}
