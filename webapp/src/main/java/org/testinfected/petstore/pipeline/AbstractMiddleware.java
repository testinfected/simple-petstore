package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Handler;

public abstract class AbstractMiddleware implements Middleware {

    private Handler handler = new Handler() {
        public void handle(Request request, Response response) throws Exception {
        }
    };

    public void wrap(Handler handler) {
        this.handler = handler;
    }

    protected void forward(Request request, Response response) throws Exception {
        handler.handle(request, response);
    }
}
