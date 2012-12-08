package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.util.RequestWrapper;
import org.testinfected.molecule.Response;

import java.io.IOException;

public class HttpMethodOverride extends AbstractMiddleware {

    public static String METHOD_OVERRIDE_PARAMETER = "_method";

    public void handle(Request request, Response response) throws Exception {
        if (overrideDetected(request)) {
            request = overrideMethod(request);
        }
        forward(request, response);
    }

    private Request overrideMethod(final Request request) throws IOException {
        if (HttpMethod.valid(methodOverride(request))) {
            return new RequestWrapper(request) {
                public String method() {
                    return methodOverride(request).toUpperCase();
                }
            };
        }
        return request;
    }

    private boolean overrideDetected(Request request) throws IOException {
        return methodOverride(request) != null;
    }

    private String methodOverride(Request request) {
        return request.parameter(METHOD_OVERRIDE_PARAMETER);
    }
}
