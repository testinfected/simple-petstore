package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.routing.Route;
import org.testinfected.molecule.routing.RouteBuilder;
import org.testinfected.molecule.routing.RouteSet;

import java.util.ArrayList;
import java.util.List;

import static org.testinfected.molecule.middlewares.NotFound.notFound;

public class Routes implements Application, RouteSet {

    private Application defaultApp;

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
        this.defaultApp = fallback;
    }

    public Routes defaultsTo(Application app) {
        this.defaultApp = app;
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

    public void handle(Request request, Response response) throws Exception {
        Route route = routeFor(request);
        if (route != null)
            route.handle(request, response);
        else
            defaultApp.handle(request, response);
    }
}