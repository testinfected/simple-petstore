package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.util.HttpMethod;

import java.io.IOException;

public class StaticRoute implements Route {
    private final String path;
    private final HttpMethod method;
    private final EndPoint endPoint;

    public StaticRoute(String path, HttpMethod method, EndPoint endPoint) {
        this.path = path;
        this.method = method;
        this.endPoint = endPoint;
    }

    public boolean matches(Request request) {
        return request.getPath().getPath().startsWith(path) &&
                (method == HttpMethod.any || request.getMethod().equalsIgnoreCase(method.name()));
    }

    public void dispatch(Request request, Response response, Dispatcher dispatcher) throws IOException {
        endPoint.handle(request, response, dispatcher);
    }
}
