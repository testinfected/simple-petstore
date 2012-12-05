package org.testinfected.support.routing;

import org.testinfected.support.*;
import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.matchers.Combination;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static org.testinfected.support.matchers.Matchers.*;

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
        app.handle(new BoundParameters(request.unwrap(org.simpleframework.http.Request.class)), response.unwrap(org.simpleframework.http.Response.class));
    }

    public class BoundParameters extends org.simpleframework.http.RequestWrapper {
        private final Map<String, String> boundParameters;

        public BoundParameters(org.simpleframework.http.Request request) {
            super(request);
            boundParameters = path.boundParameters(request.getPath().getPath());
        }

        public String getParameter(String name) throws IOException {
            if (boundParameters.containsKey(name)) return boundParameters.get(name);
            else return super.getParameter(name);
        }
    }
}
