package org.testinfected.petstore.routing;

public interface RouteSet {

    void add(Route route);

    void setDefaultRoute(Route route);
}
