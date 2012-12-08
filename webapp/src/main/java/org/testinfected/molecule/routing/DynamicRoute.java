package org.testinfected.molecule.routing;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.util.Matcher;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.util.RequestWrapper;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.matchers.Combination;

import java.util.Map;

import static org.testinfected.molecule.matchers.Matchers.withMethod;
import static org.testinfected.molecule.matchers.Matchers.withPath;

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
