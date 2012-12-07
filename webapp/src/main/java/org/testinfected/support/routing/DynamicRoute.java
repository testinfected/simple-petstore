package org.testinfected.support.routing;

import org.testinfected.support.Application;
import org.testinfected.support.Matcher;
import org.testinfected.support.Request;
import org.testinfected.support.RequestWrapper;
import org.testinfected.support.Response;
import org.testinfected.support.SimpleRequest;
import org.testinfected.support.SimpleResponse;
import org.testinfected.support.matchers.Combination;

import java.nio.charset.Charset;
import java.util.Map;

import static org.testinfected.support.matchers.Matchers.withMethod;
import static org.testinfected.support.matchers.Matchers.withPath;

public class DynamicRoute implements Route {

    private final DynamicPath path;
    private final Matcher<? super String> method;
    private final Application app;

    public DynamicRoute(String pathPattern, Matcher<? super String> method, Application app) {
        this.path = new DynamicPath(pathPattern);
        this.method = method;
        this.app = app;
    }

    public boolean matches(Request request) {
        return both(withMethod(method)).and(withPath(path)).matches(request);
    }

    private Combination<Request> both(Matcher<Request> matcher) {
        return Combination.both(matcher);
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        app.handle(new BoundParameters(request), response);
    }

    public class BoundParameters extends RequestWrapper {
        private final Map<String, String> boundParameters;

        public BoundParameters(Request request) {
            super(request);
            boundParameters = path.boundParameters(request.pathInfo());
        }

        public String parameter(String name) {
            if (boundParameters.containsKey(name)) return boundParameters.get(name);
            else return super.parameter(name);
        }
    }
}
