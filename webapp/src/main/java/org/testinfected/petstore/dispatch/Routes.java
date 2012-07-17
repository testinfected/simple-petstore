package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;

import java.util.HashMap;
import java.util.Map;

public class Routes {

    private final Map<Route, Action> routes = new HashMap<Route, Action>();
    
    public Routes() {}

    public void draw(Routing routing, Action action) {
        connect(routing.draw(), action);
    }

    public void connect(Route route, Action action) {
        routes.put(route, action);
    }

    public Action select(Request request) {
        for (Route route : routes.keySet()) {
            if (route.handles(request)) return routes.get(route);
        }
        return null;
    }
}