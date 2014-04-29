package com.vtence.molecule.routing;

import com.vtence.molecule.Application;
import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.lib.Matcher;
import com.vtence.molecule.lib.Matchers;

import java.util.ArrayList;
import java.util.List;

import static com.vtence.molecule.lib.Matchers.anyOf;
import static com.vtence.molecule.lib.Matchers.equalTo;

public class DynamicRouteDefinition implements RouteDefinition, ViaClause {

    private Matcher<? super String> path;
    private Matcher<? super HttpMethod> method = Matchers.<HttpMethod>anything();
    private Application app;

    public DynamicRouteDefinition map(String path) {
        return map(new DynamicPath(path));
    }

    public DynamicRouteDefinition map(Matcher<? super String> path) {
        this.path = path;
        return this;
    }

    public DynamicRouteDefinition via(HttpMethod... methods) {
        return via(anyOf(equallyMatching(methods)));
    }

    private List<Matcher<? super HttpMethod>> equallyMatching(HttpMethod... methods) {
        List<Matcher<? super HttpMethod>> matchMethods =
                new ArrayList<Matcher<? super HttpMethod>>();
        for (HttpMethod httpMethod : methods) {
            matchMethods.add(equalTo(httpMethod));
        }
        return matchMethods;
    }

    public DynamicRouteDefinition via(Matcher<? super HttpMethod> method) {
        this.method = method;
        return this;
    }

    public DynamicRouteDefinition to(Application application) {
        this.app = application;
        return this;
    }

    public DynamicRoute toRoute() {
        ensureValid();
        return new DynamicRoute(path, method, app);
    }

    public void ensureValid() {
        if (path == null) throw new IllegalStateException("No path was specified");
    }
}
