package com.vtence.molecule.routing;

import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.lib.Matcher;

import java.util.ArrayList;
import java.util.Collection;

public class DynamicRoutes implements RouteBuilder {

    private final Collection<DynamicRouteDefinition> routes = new ArrayList<DynamicRouteDefinition>();

    public void build(RouteSet routeSet) {
        for (DynamicRouteDefinition definition : this.routes) {
            routeSet.add(definition.toRoute());
        }
    }

    public ViaClause map(String path) {
        return openRoute().map(path);
    }

    public ViaClause map(Matcher<? super String> path) {
        return openRoute().map(path);
    }

    public ToClause get(String path) {
        return map(path).via(HttpMethod.GET);
    }

    public ToClause get(Matcher<? super String> path) {
        return map(path).via(HttpMethod.GET);
    }

    public ToClause post(String path) {
        return map(path).via(HttpMethod.POST);
    }

    public ToClause post(Matcher<? super String> path) {
        return map(path).via(HttpMethod.POST);
    }

    public ToClause put(String path) {
        return map(path).via(HttpMethod.PUT);
    }

    public ToClause put(Matcher<? super String> path) {
        return map(path).via(HttpMethod.PUT);
    }

    public ToClause delete(String path) {
        return map(path).via(HttpMethod.DELETE);
    }

    public ToClause delete(Matcher<? super String> path) {
        return map(path).via(HttpMethod.DELETE);
    }

    private RouteDefinition openRoute() {
        DynamicRouteDefinition definition = new DynamicRouteDefinition();
        routes.add(definition);
        return definition;
    }
}
