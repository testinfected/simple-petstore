package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.pipeline.Middleware;
import org.testinfected.petstore.pipeline.NotFound;

import java.util.ArrayList;
import java.util.List;

public class Routes implements Middleware, RouteSet {

    private final List<Route> routingTable = new ArrayList<Route>();
    private Application fallback = NotFound.notFound();

    public static Routes draw(RouteBuilder routeBuilder) {
        Routes routes = new Routes();
        routeBuilder.build(routes);
        return routes;
    }

    public void add(Route route) {
        routingTable.add(route);
    }

    private Application routeFor(Request request) {
        for (Route route : routingTable) {
            if (route.matches(request)) return route;
        }
        return fallback;
    }

    public void handle(Request request, Response response) throws Exception {
        routeFor(request).handle(request, response);
    }

    public void chain(Application app) {
        this.fallback = app;
    }

    public Routes defaultsTo(Application app) {
        chain(app);
        return this;
    }
}