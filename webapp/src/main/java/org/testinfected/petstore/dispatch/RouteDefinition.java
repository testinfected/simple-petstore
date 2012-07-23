package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public interface RouteDefinition {

    RouteDefinition map(String path);

    RouteDefinition via(HttpMethod method);

    RouteDefinition to(EndPoint endPoint);
}
