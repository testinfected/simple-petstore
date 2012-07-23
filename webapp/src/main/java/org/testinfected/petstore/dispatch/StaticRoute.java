package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public class StaticRoute implements Route {
    private final String path;
    private final HttpMethod method;
    private final EndPoint endPoint;

    public StaticRoute(String path, HttpMethod method, EndPoint endPoint) {
        this.path = path;
        this.method = method;
        this.endPoint = endPoint;
    }

    public boolean matches(Dispatch.Request request) {
        return request.getPath().startsWith(path) &&
                (method == HttpMethod.any || request.getMethod().equalsIgnoreCase(method.name()));
    }

    public void dispatch(Dispatch.Request request, Dispatch.Response response) throws Exception {
        endPoint.process(request, response);
    }
}
