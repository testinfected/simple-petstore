package org.testinfected.petstore.routing;

import org.testinfected.petstore.util.HttpMethod;

import java.util.ArrayList;
import java.util.Collection;

public class Router implements RouteBuilder {

    private final Collection<DynamicRouteDefinition> routes = new ArrayList<DynamicRouteDefinition>();

    public void build(RouteSet routeSet) {
        for (DynamicRouteDefinition route : this.routes) {
            routeSet.add(route.draw());
        }
    }

    public RouteDefinition map(String path) {
        return openRoute().map(path);
    }

    public RouteDefinition post(String path) {
        return map(path).via(HttpMethod.POST);
    }

    public RouteDefinition get(String path) {
        return map(path).via(HttpMethod.GET);
    }

    public RouteDefinition delete(String path) {
        return map(path).via(HttpMethod.DELETE);
    }

    private RouteDefinition openRoute() {
        DynamicRouteDefinition definition = DynamicRouteDefinition.route();
        routes.add(definition);
        return definition;
    }
}
