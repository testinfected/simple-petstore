package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.testinfected.petstore.util.HttpMethod;

public class Routing {

    private String path;
    private HttpMethod method = HttpMethod.any;

    public Route draw() {
        return new Route() {
            public boolean handles(Request request) {
                return request.getPath().getPath().startsWith(path) &&
                        (method == HttpMethod.any || request.getMethod().equalsIgnoreCase(method.name()));
            }
        };
    }

    public static Routing match(String path) {
        Routing routing = new Routing();
        routing.path(path);
        return routing;
    }

    private void path(String path) {
        this.path = path;
    }

    public static Routing delete(String path) {
        return match(path).via(HttpMethod.delete);
    }

    private Routing via(HttpMethod method) {
        this.method = method;
        return this;
    }
}
