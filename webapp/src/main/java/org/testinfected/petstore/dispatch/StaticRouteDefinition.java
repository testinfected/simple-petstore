package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public class StaticRouteDefinition implements RouteDefinition {

    public static StaticRouteDefinition staticRoute() {
        return new StaticRouteDefinition();
    }

    private HttpMethod method = HttpMethod.any;
    private String path = "";
    private EndPoint endPoint;

    public StaticRouteDefinition map(String path) {
        this.path = path;
        return this;
    }

    public StaticRouteDefinition via(HttpMethod method) {
        this.method = method;
        return this;
    }

    public StaticRouteDefinition to(EndPoint endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public StaticRoute toRoute() {
        return new StaticRoute(path, method, endPoint);
    }
}
