package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class MiddlewareStack implements Application {

    private final Deque<Middleware> stack = new ArrayDeque<Middleware>();
    private Application runner;
    private Application chain;

    public MiddlewareStack() {}

    public void use(Middleware middleware) {
        stack.add(middleware);
    }

    public void run(Application app) {
        this.runner = app;
    }

    public void handle(Request request, Response response) throws Exception {
        if (!assembled()) assemble();

        chain.handle(request, response);
    }

    public Application assemble() {
        if (runner == null) throw new IllegalStateException("No runner specified");

        chain = runner;
        for (Iterator<Middleware> middlewares = stack.descendingIterator(); middlewares.hasNext(); ) {
            Middleware previous = middlewares.next();
            previous.chain(chain);
            chain = previous;
        }
        return chain;
    }

    private boolean assembled() {
        return chain != null;
    }
}
