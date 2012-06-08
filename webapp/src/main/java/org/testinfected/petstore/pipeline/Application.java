package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Handler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Application implements Handler {

    private final Deque<Middleware> stack = new ArrayDeque<Middleware>();
    private Handler runner;

    public Application() {
    }

    public void run(Handler runner) {
        this.runner = runner;
    }

    public void use(Middleware middleware) {
        stack.add(middleware);
    }

    public void handle(Request request, Response response) throws Exception {
        chain().handle(request, response);
    }

    public Handler chain() {
        if (runner == null) throw new IllegalStateException("No runner specified");

        Handler chain = runner;
        for (Iterator<Middleware> middlewares = stack.descendingIterator(); middlewares.hasNext(); ) {
            Middleware previous = middlewares.next();
            previous.wrap(chain);
            chain = previous;
        }
        return chain;
    }
}
