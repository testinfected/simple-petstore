package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public class StaticRouteDefinition implements RouteDefinition {

    private HttpMethod method = HttpMethod.any;
    private String path = "";
    private Destination destination;

    public StaticRouteDefinition map(String path) {
        this.path = path;
        return this;
    }

    public StaticRouteDefinition via(HttpMethod method) {
        this.method = method;
        return this;
    }

    public StaticRouteDefinition to(Destination destination) {
        this.destination = destination;
        return this;
    }

    public Route toRoute() {
        return new StaticRoute(path, method, destination);
    }
}
