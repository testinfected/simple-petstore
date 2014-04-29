package com.vtence.molecule.routing;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.matchers.Combination;
import com.vtence.molecule.util.Matcher;
import com.vtence.molecule.util.RequestWrapper;

import java.util.Map;

import static com.vtence.molecule.matchers.Matchers.withMethod;
import static com.vtence.molecule.matchers.Matchers.withPath;

public class DynamicRoute implements Route {

    private final Matcher<? super String> path;
    private final Matcher<? super String> method;
    private final Application app;

    public DynamicRoute(Matcher<? super String> path, Matcher<? super String> method,
                        Application app) {
        this.path = path;
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
        Request wrapper = path instanceof WithBoundParameters ?
                new RequestWithPathBoundParameters(request, (WithBoundParameters) path) : request;
        app.handle(wrapper, response);
    }

    public class RequestWithPathBoundParameters extends RequestWrapper {
        private final Map<String, String> boundParameters;

        public RequestWithPathBoundParameters(Request request, WithBoundParameters path) {
            super(request);
            boundParameters = path.boundParameters(request.pathInfo());
        }

        public String parameter(String name) {
            if (boundParameters.containsKey(name)) return boundParameters.get(name);
            else return super.parameter(name);
        }
    }
}
