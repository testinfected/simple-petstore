package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.RequestMatcher;

public class StaticRoute implements Route {

    private final Application app;
    private final RequestMatcher matcher;

    public StaticRoute(RequestMatcher requestMatcher, Application application) {
        this.matcher = requestMatcher;
        this.app = application;
    }

    public boolean matches(Request request) {
        return matcher.matches(request);
    }

    public void handle(Request request, Response response) throws Exception {
        app.handle(request, response);
    }
}
