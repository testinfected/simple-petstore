package org.testinfected.petstore.routing;

import org.testinfected.petstore.util.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class Routes implements RouteBuilder {

    private final List<StaticRouteDefinition> definitions = new ArrayList<StaticRouteDefinition>();
    private StaticRouteDefinition defaultRouteDefinition;

    public void defineRoutes(RouteSet routes) {
        for (StaticRouteDefinition definition : this.definitions) {
            routes.add(definition.toRoute());
        }
        routes.setDefaultRoute(defaultRouteDefinition.toRoute());
    }

    public RouteDefinition map(String path) {
        return openRoute().map(path);
    }

    public RouteDefinition delete(String path) {
        return map(path).via(HttpMethod.delete);
    }

    public RouteDefinition otherwise() {
        defaultRouteDefinition = openRoute();
        return defaultRouteDefinition;
    }

    private StaticRouteDefinition openRoute() {
        StaticRouteDefinition definition = new StaticRouteDefinition();
        definitions.add(definition);
        return definition;
    }
}
