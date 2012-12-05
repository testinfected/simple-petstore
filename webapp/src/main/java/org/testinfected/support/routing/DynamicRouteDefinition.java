package org.testinfected.support.routing;

import org.testinfected.support.Application;
import org.testinfected.support.HttpMethod;
import org.testinfected.support.Matcher;

import static org.testinfected.support.matchers.Matchers.anyMethod;
import static org.testinfected.support.matchers.Matchers.equalTo;

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
