package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private final RoutingTable routingTable = new RoutingTable();

    public void draw(RouteBuilder routeBuilder) {
        routeBuilder.defineRoutes(routingTable);
    }

    public void handle(Request request, Response response) throws Exception {
        routingTable.locateRoute(request).handle(request, response);
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