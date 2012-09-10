package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.Combination;
import org.testinfected.petstore.util.HttpMethod;
import org.testinfected.petstore.util.Matcher;

import static org.testinfected.petstore.util.Combination.both;
import static org.testinfected.petstore.util.Matchers.anyMethod;
import static org.testinfected.petstore.util.Matchers.anyPath;
import static org.testinfected.petstore.util.Matchers.equalTo;
import static org.testinfected.petstore.util.Matchers.hasMethod;
import static org.testinfected.petstore.util.Matchers.hasNormalizedPath;
import static org.testinfected.petstore.util.Matchers.startingWith;

public class StaticRouteDefinition implements RouteDefinition, Matcher<Request> {

    public static StaticRouteDefinition staticRoute() {
        return new StaticRouteDefinition();
    }

    private Matcher<Request> method = hasMethod(anyMethod());
    private Matcher<Request> path = hasNormalizedPath(anyPath());
    private Application app;

    public StaticRouteDefinition map(String path) {
        return map(startingWith(path));
    }

    public StaticRouteDefinition map(Matcher<String> path) {
        this.path = hasNormalizedPath(path);
        return this;
    }

    public StaticRouteDefinition via(HttpMethod method) {
        return via(equalTo(method.name()));
    }

    public StaticRouteDefinition via(Matcher<String> method) {
        this.method = hasMethod(method);
        return this;
    }

    public RouteDefinition to(Application application) {
        this.app = application;
        return this;
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Request request) {
        return both(method).and(path).matches(request);
    }

    public StaticRoute toRoute() {
        return new StaticRoute(this, app);
    }

    private Combination<Request> both(Matcher<Request> matcher) {
        return Combination.both(matcher);
    }
}
