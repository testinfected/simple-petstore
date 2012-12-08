package org.testinfected.molecule.routing;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpMethod;

// todo consider having separate clauses
public interface RouteDefinition {

    RouteDefinition map(String path);

    RouteDefinition via(HttpMethod method);

    RouteDefinition to(Application application);
}
