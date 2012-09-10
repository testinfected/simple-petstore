package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.pipeline.NotFound;

import java.util.ArrayList;
import java.util.List;

import static org.testinfected.petstore.util.Matchers.anyRequest;

public class Routes implements RouteSet, Application {

    private final List<Route> routingTable = new ArrayList<Route>();
    private Application fallback = new NotFound();

    public void add(Route route) {
        routingTable.add(route);
    }

    public void draw(RouteBuilder routeBuilder) {
        routeBuilder.build(this);
    }

    private Route routeMatching(Request request) {
        for (Route route : routingTable) {
            if (route.matches(request)) return route;
        }
        return new StaticRoute(anyRequest(), fallback);
    }

    public void handle(Request request, Response response) throws Exception {
        routeMatching(request).handle(request, response);
    }

    public void fallbackTo(Application application) {
        this.fallback = application;
    }
}