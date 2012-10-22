package org.testinfected.petstore.routing;

import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.HttpMethod;
import org.testinfected.petstore.util.Matcher;

// todo consider having separate clauses
public interface RouteDefinition {

    RouteDefinition map(String path);

    RouteDefinition map(Matcher<? super String> path);

    RouteDefinition via(HttpMethod method);

    RouteDefinition via(Matcher<String> method);

    RouteDefinition to(Application application);
}
