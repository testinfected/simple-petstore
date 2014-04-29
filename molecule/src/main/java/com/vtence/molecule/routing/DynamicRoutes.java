package com.vtence.molecule.routing;

import com.vtence.molecule.HttpMethod;

import java.util.ArrayList;
import java.util.Collection;

public class DynamicRoutes implements RouteBuilder {

    private final Collection<DynamicRouteDefinition> routes = new ArrayList<DynamicRouteDefinition>();

    public void build(RouteSet routeSet) {
        for (DynamicRouteDefinition route : this.routes) {
            routeSet.add(route.toRoute());
        }
    }

    public ViaClause map(String path) {
        return openRoute().map(path);
    }

    public ToClause post(String path) {
        return map(path).via(HttpMethod.POST);
    }

    public ToClause get(String path) {
        return map(path).via(HttpMethod.GET);
    }

    public ToClause delete(String path) {
        return map(path).via(HttpMethod.DELETE);
    }

    private RouteDefinition openRoute() {
        DynamicRouteDefinition definition = new DynamicRouteDefinition();
        routes.add(definition);
        return definition;
    }
}
