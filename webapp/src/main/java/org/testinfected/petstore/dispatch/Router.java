package org.testinfected.petstore.dispatch;

import java.util.ArrayList;
import java.util.List;

public class Router implements Routing {

    private final RoutingTable routingTable = new RoutingTable();

    public void draw(RouteBuilder routeBuilder) {
        routeBuilder.defineRoutes(routingTable);
    }

    public void dispatch(Dispatch.Request request, Dispatch.Response response) throws Exception {
        routingTable.locateRoute(request).dispatch(request, response);
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

        public Route locateRoute(Dispatch.Request request) {
            for (Route route : routingTable) {
                if (route.matches(request)) return route;
            }
            return defaultRoute;
        }
    }
}