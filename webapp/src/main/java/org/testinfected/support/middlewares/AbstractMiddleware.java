package org.testinfected.support.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.Middleware;
import org.testinfected.support.SimpleRequest;
import org.testinfected.support.SimpleResponse;

import java.nio.charset.Charset;

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
