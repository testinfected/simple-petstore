package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.routing.Route;
import com.vtence.molecule.routing.RouteBuilder;
import com.vtence.molecule.routing.RouteSet;

import java.util.ArrayList;
import java.util.List;

public class Router implements Application, RouteSet {

    private Application defaultApp;

    public static Router draw(RouteBuilder routeBuilder) {
        Router router = new Router();
        routeBuilder.build(router);
        return router;
    }

    private final List<Route> routingTable = new ArrayList<Route>();

    public Router() {
        this(new NotFound());
    }

    public Router(final Application fallback) {
        defaultsTo(fallback);
    }

    public Router defaultsTo(Application app) {
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