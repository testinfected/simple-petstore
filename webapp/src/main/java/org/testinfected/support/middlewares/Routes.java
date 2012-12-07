package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.support.routing.Route;
import org.testinfected.support.routing.RouteBuilder;
import org.testinfected.support.routing.RouteSet;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.testinfected.support.middlewares.NotFound.notFound;

public class Routes extends AbstractMiddleware implements RouteSet {

    public static Routes draw(RouteBuilder routeBuilder) {
        Routes routes = new Routes();
        routeBuilder.build(routes);
        return routes;
    }

    private final List<Route> routingTable = new ArrayList<Route>();

    public Routes() {
        this(notFound());
    }

    public Routes(final Application fallback) {
        connectTo(fallback);
    }

    public Routes defaultsTo(Application app) {
        connectTo(app);
        return this;
    }

    public void add(Route route) {
        routingTable.add(route);
    }

    private Route routeFor(Request request) {
        for (Route route : routingTable) {
            if (route.matches(request)) return route;
        }
        return null;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        Route route = routeFor(request);
        if (route != null)
            route.handle(request, response);
        else
            forward(request, response);
    }

    protected void forward(Request request, Response response) throws Exception {
        successor.handle(request, response);
    }
}