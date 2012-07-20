package org.testinfected.petstore.dispatch;

public interface RouteSet {

    void add(Route route);

    void setDefaultRoute(Route route);
}
