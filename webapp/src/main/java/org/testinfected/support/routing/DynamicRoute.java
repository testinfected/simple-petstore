package org.testinfected.support.routing;

import org.simpleframework.http.RequestWrapper;
import org.testinfected.support.*;
import org.testinfected.support.matchers.Combination;

import java.io.IOException;
import java.nio.charset.Charset;
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
        org.simpleframework.http.Request rq = request.unwrap(org.simpleframework.http.Request.class);
        return both(hasMethod(method)).and(hasPath(path)).matches(rq);
    }

    private Combination<org.simpleframework.http.Request> both(Matcher<org.simpleframework.http.Request> matcher) {
        return Combination.both(matcher);
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        final Map<String, String> boundParameters = path.extractBoundParameters(request.pathInfo());

        app.handle(new RequestWrapper(request.unwrap(org.simpleframework.http.Request.class)) {
            public String getParameter(String name) throws IOException {
                if (boundParameters.containsKey(name)) return boundParameters.get(name);
                else return super.getParameter(name);
            }
        }, response.unwrap(org.simpleframework.http.Response.class));
    }
}
