package org.testinfected.petstore.routing;

import org.testinfected.petstore.util.HttpMethod;

import java.util.ArrayList;
import java.util.Collection;

public class Router implements RouteBuilder {

    private final Collection<StaticRouteDefinition> definitions = new ArrayList<StaticRouteDefinition>();

    public void build(RouteSet routes) {
        for (StaticRouteDefinition definition : this.definitions) {
            routes.add(definition.toRoute());
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
        StaticRouteDefinition definition = new StaticRouteDefinition();
        definitions.add(definition);
        return definition;
    }
}
