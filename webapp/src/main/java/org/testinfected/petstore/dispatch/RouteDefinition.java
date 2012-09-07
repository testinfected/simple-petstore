package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public interface RouteDefinition {

    //todo consider adding a match(Matcher<Request>

    RouteDefinition map(String path);

    RouteDefinition via(HttpMethod method);

    RouteDefinition to(EndPoint endPoint);
}
