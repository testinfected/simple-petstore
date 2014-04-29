package com.vtence.molecule.lib;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

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
        if (runner == null) throw new IllegalStateException("Nothing to run");

        chain = runner;
        for (Iterator<Middleware> middlewares = stack.descendingIterator(); middlewares.hasNext(); ) {
            Middleware previous = middlewares.next();
            previous.connectTo(chain);
            chain = previous;
        }
        return chain;
    }

    private boolean assembled() {
        return chain != null;
    }
}
