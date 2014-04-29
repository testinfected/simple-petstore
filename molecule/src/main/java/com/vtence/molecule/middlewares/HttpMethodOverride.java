package com.vtence.molecule.middlewares;

import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;

import java.io.IOException;

public class HttpMethodOverride extends AbstractMiddleware {

    public static final String METHOD_OVERRIDE_PARAMETER = "_method";

    public void handle(Request request, Response response) throws Exception {
        if (overrideDetected(request) && validOverride(request)) {
            request.method(methodOverride(request).toUpperCase());
        }
        forward(request, response);
    }

    private boolean validOverride(Request request) {
        return HttpMethod.valid(methodOverride(request));
    }

    private boolean overrideDetected(Request request) throws IOException {
        return methodOverride(request) != null && request.method() == HttpMethod.POST;
    }

    private String methodOverride(Request request) {
        return request.parameter(METHOD_OVERRIDE_PARAMETER);
    }
}
