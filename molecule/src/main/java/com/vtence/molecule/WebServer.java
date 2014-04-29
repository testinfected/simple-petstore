package com.vtence.molecule;

import com.vtence.molecule.middlewares.FilterMap;
import com.vtence.molecule.lib.Middleware;
import com.vtence.molecule.lib.MiddlewareStack;
import com.vtence.molecule.middlewares.Router;
import com.vtence.molecule.routing.RouteBuilder;
import com.vtence.molecule.servers.SimpleServer;

import java.io.IOException;
import java.net.URI;

public class WebServer {

    public static final String LOCAL_ADDRESS = "0.0.0.0";
    public static final int DEFAULT_PORT = 8080;

    private final Server server;
    private final MiddlewareStack stack;

    public static WebServer create() {
        return create(DEFAULT_PORT);
    }

    public static WebServer create(int port) {
        return create(LOCAL_ADDRESS, port);
    }

    public static WebServer create(String host, int port) {
        return new WebServer(new SimpleServer(host, port));
    }

    public WebServer(Server server) {
        this.server = server;
        this.stack = new MiddlewareStack();
    }

    public WebServer failureReporter(FailureReporter reporter) {
        server.reportErrorsTo(reporter);
        return this;
    }

    public WebServer add(Middleware middleware) {
        stack.use(middleware);
        return this;
    }

    public WebServer filter(String path, Middleware filter) {
        stack.use(new FilterMap().map(path, filter));
        return this;
    }

    public Server start(RouteBuilder routes) throws IOException {
        return start(Router.draw(routes));
    }

    public Server start(Application application) throws IOException {
        stack.run(application);
        server.run(stack);
        return server;
    }

    public void stop() throws IOException {
        server.shutdown();
    }

    public URI uri() {
        return URI.create("http://" + server.host() + ":" + server.port());
    }
}