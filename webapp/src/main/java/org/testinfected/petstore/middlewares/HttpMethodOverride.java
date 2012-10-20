package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class HttpMethodOverride extends AbstractMiddleware {

    private static final Collection<String> OVERRIDE_METHODS = Arrays.asList("PUT", "DELETE");

    public static String METHOD_OVERRIDE_PARAMETER = "_method";

    public void handle(Request request, Response response) throws Exception {
        if (methodOverriden(request)) {
            request = methodOverride(request);
        }
        forward(request, response);
    }

    private Request methodOverride(final Request request) throws IOException {
        final String methodOverride = request.getParameter(METHOD_OVERRIDE_PARAMETER);
        if (supported(methodOverride)) {
            return new RequestWrapper(request) {
                public String getMethod() {
                    return methodOverride.toUpperCase();
                }
            };
        }
        return request;
    }

    private boolean supported(String methodOverride) {
        return OVERRIDE_METHODS.contains(methodOverride.toUpperCase());
    }

    private boolean methodOverriden(Request request) throws IOException {
        return request.getParameter(METHOD_OVERRIDE_PARAMETER) != null;
    }
}
