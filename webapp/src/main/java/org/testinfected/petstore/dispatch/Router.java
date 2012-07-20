package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Router implements Routing {

    private final RoutingTable routingTable = new RoutingTable();

    public void draw(RouteBuilder routeBuilder) {
        routeBuilder.defineRoutes(routingTable);
    }

    public void dispatch(Request request, Response response, Dispatcher dispatcher) throws IOException {
        routingTable.locateRoute(request).dispatch(request, response, dispatcher);
    }

    public static class RoutingTable implements RouteSet {

        private final List<Route> routingTable = new ArrayList<Route>();
        private Route defaultRoute;

        public void add(Route route) {
            routingTable.add(route);
        }

        public void setDefaultRoute(Route route) {
            this.defaultRoute = route;
        }

        public Route locateRoute(Request request) {
            for (Route route : routingTable) {
                if (route.matches(request)) return route;
            }
            return defaultRoute;
        }
    }
}