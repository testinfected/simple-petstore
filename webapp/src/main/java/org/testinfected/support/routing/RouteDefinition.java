package org.testinfected.support.routing;

import org.testinfected.support.Application;
import org.testinfected.support.HttpMethod;

// todo consider having separate clauses
public interface RouteDefinition {

    RouteDefinition map(String path);

    RouteDefinition via(HttpMethod method);

    RouteDefinition to(Application application);
}
