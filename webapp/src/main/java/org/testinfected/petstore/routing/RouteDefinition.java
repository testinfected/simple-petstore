package org.testinfected.petstore.routing;

import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.HttpMethod;

// todo consider having separate clauses
public interface RouteDefinition {

    //todo Add a match(Matcher<Path>)

    RouteDefinition map(String path);

    RouteDefinition via(HttpMethod method);

    RouteDefinition to(Application application);
}
