package org.testinfected.petstore.routing;

import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.HttpMethod;
import org.testinfected.petstore.util.Matcher;

import static org.testinfected.petstore.util.Matchers.anyMethod;
import static org.testinfected.petstore.util.Matchers.equalTo;

public class DynamicRouteDefinition implements RouteDefinition {

    public static DynamicRouteDefinition route() {
        return new DynamicRouteDefinition();
    }

    private DynamicPath path;
    private Matcher<? super String> method = anyMethod();
    private Application app;

    public DynamicRouteDefinition map(String path) {
        this.path = new DynamicPath(path);
        return this;
    }

    public DynamicRouteDefinition via(HttpMethod method) {
        return via(equalTo(method.name()));
    }

    public DynamicRouteDefinition via(Matcher<? super String> method) {
        this.method = method;
        return this;
    }

    public DynamicRouteDefinition to(Application application) {
        this.app = application;
        return this;
    }

    public DynamicRoute draw() {
        checkValidity();
        return new DynamicRoute(path, method, app);
    }

    public void checkValidity() {
        if (path == null) throw new IllegalStateException("No path was specified");
    }
}
