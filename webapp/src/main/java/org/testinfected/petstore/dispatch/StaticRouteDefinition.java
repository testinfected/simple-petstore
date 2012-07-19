package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public class StaticRouteDefinition implements RouteDefinition {

    private HttpMethod method = HttpMethod.any;
    private String path;
    private Destination destination;

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Route toRoute() {
        return new StaticRoute(path, method, destination);
    }
}
