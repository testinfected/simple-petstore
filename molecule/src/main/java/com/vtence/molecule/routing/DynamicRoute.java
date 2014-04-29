package com.vtence.molecule.routing;

import com.vtence.molecule.Application;
import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.lib.Matcher;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.Combination;
import com.vtence.molecule.lib.Matchers;

import java.util.Map;

public class DynamicRoute implements Route {

    private final Matcher<? super String> path;
    private final Matcher<? super HttpMethod> method;
    private final Application app;

    public DynamicRoute(Matcher<? super String> path, Matcher<? super HttpMethod> method,
                        Application app) {
        this.path = path;
        this.method = method;
        this.app = app;
    }

    public boolean matches(Request request) {
        return both(Matchers.withMethod(method)).and(Matchers.withPath(path)).matches(request);
    }

    private Combination<Request> both(Matcher<Request> matcher) {
        return Combination.both(matcher);
    }

    public void handle(Request request, Response response) throws Exception {
        if (path instanceof WithBoundParameters) {
            WithBoundParameters dynamicPath = (WithBoundParameters) path;
            Map<String, String> dynamicParameters = dynamicPath.parametersBoundTo(request.path());
            for (String name: dynamicParameters.keySet()  ) {
                request.addParameter(name, dynamicParameters.get(name));
            }
        }
        app.handle(request, response);
    }
}
