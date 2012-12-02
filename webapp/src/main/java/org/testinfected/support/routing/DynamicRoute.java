package org.testinfected.support.routing;

import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.matchers.Combination;
import org.testinfected.support.Matcher;

import java.io.IOException;
import java.util.Map;

import static org.testinfected.support.matchers.Matchers.hasMethod;
import static org.testinfected.support.matchers.Matchers.hasPath;

public class DynamicRoute implements Route {

    private final DynamicPath path;
    private final Matcher<? super String> method;
    private final Application app;

    public DynamicRoute(DynamicPath path, Matcher<? super String> method, Application app) {
        this.path = path;
        this.method = method;
        this.app = app;
    }

    public boolean matches(Request request) {
        return both(hasMethod(method)).and(hasPath(path)).matches(request);
    }

    private Combination<Request> both(Matcher<Request> matcher) {
        return Combination.both(matcher);
    }

    public void handle(Request request, Response response) throws Exception {
        final Map<String, String> boundParameters = path.extractBoundParameters(request.getPath());

        app.handle(new RequestWrapper(request) {
            public String getParameter(String name) throws IOException {
                if (boundParameters.containsKey(name)) return boundParameters.get(name);
                else return super.getParameter(name);
            }
        }, response);
    }
}
