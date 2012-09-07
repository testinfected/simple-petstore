package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.HttpMethod;
import org.testinfected.petstore.util.RequestMatcher;

public class StaticRouteDefinition implements RouteDefinition, RequestMatcher {

    public static StaticRouteDefinition staticRoute() {
        return new StaticRouteDefinition();
    }

    private HttpMethod method = HttpMethod.any;
    private String path = "";
    private Application app;

    public StaticRouteDefinition map(String path) {
        this.path = path;
        return this;
    }

    public StaticRouteDefinition via(HttpMethod method) {
        this.method = method;
        return this;
    }

    public RouteDefinition to(Application application) {
        this.app = application;
        return this;
    }

    public boolean matches(Request request) {
        return request.getPath().getPath().startsWith(path) &&
            (method == HttpMethod.any || request.getMethod().equalsIgnoreCase(method.name()));
    }

    public StaticRoute toRoute() {
        return new StaticRoute(this, app);
    }
}
