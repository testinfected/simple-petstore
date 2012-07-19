package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Router implements RouteSet {

    private final List<Route> routingTable = new ArrayList<Route>();
    private final Destination defaultDestination;

    public Router(Destination defaultDestination) {
        this.defaultDestination = defaultDestination;
    }

    public void add(Route route) {
        routingTable.add(route);
    }

    private Route findRouteFor(Request request) {
        for (Route route : routingTable) {
            if (route.connects(request)) return route;
        }
        return null;
    }

    public void dispatch(Request request, Response response, Dispatcher dispatcher) throws IOException {
        Route route = findRouteFor(request);
        if (route != null) {
            route.dispatch(request, response, dispatcher);
        } else {
            defaultDestination.handle(request, response, dispatcher);
        }
    }
}