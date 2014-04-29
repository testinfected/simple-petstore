package com.vtence.molecule.lib;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

public abstract class AbstractMiddleware implements Middleware {

    protected static final Application NO_SUCCESSOR = new Application() {
        public void handle(Request request, Response response) throws Exception {
        }
    };

    protected Application successor;

    protected AbstractMiddleware() {
        this(NO_SUCCESSOR);
    }

    protected AbstractMiddleware(Application successor) {
        this.successor = successor;
    }

    public void connectTo(Application successor) {
        this.successor = successor;
    }

    protected void forward(Request request, Response response) throws Exception {
        successor.handle(request, response);
    }
}
