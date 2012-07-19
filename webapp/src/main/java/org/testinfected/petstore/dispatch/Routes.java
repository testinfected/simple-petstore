package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class Routes implements Routing {

    private final List<RouteDefinition> definitions = new ArrayList<RouteDefinition>();
    private RouteDefinition currentDefinition;

    public void defineRoutes(RouteSet routes) {
        for (RouteDefinition definition : this.definitions) {
            routes.add(definition.toRoute());
        }
    }

    public Routes match(String path) {
        RouteDefinition routeDefinition = openRoute();
        routeDefinition.setPath(path);
        return this;
    }

    public Routes delete(String path) {
        return match(path).via(HttpMethod.delete);
    }

    public Routes via(HttpMethod method) {
        currentDefinition.setMethod(method);
        return this;
    }

    public Routes to(Destination destination) {
        currentDefinition.setDestination(destination);
        return this;
    }

    private RouteDefinition openRoute() {
        RouteDefinition definition = new StaticRouteDefinition();
        definitions.add(definition);
        currentDefinition = definition;
        return definition;
    }
}
